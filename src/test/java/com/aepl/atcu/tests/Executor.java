package com.aepl.atcu.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.asserts.SoftAssert;

import com.aepl.atcu.util.ExcelUtility;
import com.aepl.atcu.util.Result;
import com.google.common.base.Supplier;

public class Executor {

	private static final Logger logger = LogManager.getLogger(Executor.class);
	private final ExcelUtility excelUtility;
	private final SoftAssert softAssert;

	public Executor(ExcelUtility excelUtility, SoftAssert softAssert) {
		this.excelUtility = excelUtility;
		this.softAssert = softAssert;
	}

	public <T> void executeTest(String testCaseName, T expected, Supplier<T> actualSupplier) {
		T actual = null;
		Result result;

		logger.debug("Executing test case: {}", testCaseName);

		try {
			actual = actualSupplier.get();
			boolean isPass = compareValues(expected, actual);

			if (!isPass) {
				softAssert.fail(buildFailureMessage(testCaseName, expected, actual));
				result = Result.FAIL;
			} else {
				result = Result.PASS;
			}

		} catch (Exception e) {
			logger.error("Exception during test execution: {}", testCaseName, e);
			result = Result.ERROR;
		}

		excelUtility.writeTestDataToExcel(testCaseName, String.valueOf(expected), String.valueOf(actual),
				result.getValue());
	}

	private String buildFailureMessage(String testCaseName, Object expected, Object actual) {
		return testCaseName + " FAILED | Expected: [" + expected + "] but found: [" + actual + "]";
	}

	private boolean compareValues(Object expected, Object actual) {

		if (expected == null || actual == null) {
			return expected == actual;
		}

		// Strict string comparison
		if (expected instanceof String && actual instanceof String) {
			return expected.equals(actual);
		}

		// Arrays
		if (expected.getClass().isArray() && actual.getClass().isArray()) {
			return Arrays.deepEquals(wrapArray(expected), wrapArray(actual));
		}

		// Collections (order-sensitive)
		if (expected instanceof Collection && actual instanceof Collection) {
			return new ArrayList<>((Collection<?>) expected).equals(new ArrayList<>((Collection<?>) actual));
		}

		// Maps (key + value strict match)
		if (expected instanceof Map && actual instanceof Map) {
			return expected.equals(actual);
		}

		// Default strict equality
		return expected.equals(actual);
	}

	private Object[] wrapArray(Object array) {
		if (array instanceof Object[])
			return (Object[]) array;

		int length = java.lang.reflect.Array.getLength(array);
		Object[] wrapped = new Object[length];
		for (int i = 0; i < length; i++) {
			wrapped[i] = java.lang.reflect.Array.get(array, i);
		}
		return wrapped;
	}
}
