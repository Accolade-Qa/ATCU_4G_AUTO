package com.aepl.atcu.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static final Logger logger = LogManager.getLogger(ExtentTestManager.class);
    private static final Map<Long, ExtentTest> extentTestMap = new HashMap<>();
    private static final ExtentReports extent = ExtentManager.getInstance();

    public static synchronized ExtentTest startTest(String testName) {
        long threadId = Thread.currentThread().getId();
        logger.info("Starting test '{}' on thread ID: {}", testName, threadId);

        ExtentTest test = extent.createTest(testName);
        extentTestMap.put(threadId, test);

        logger.debug("Test instance stored in extentTestMap for thread ID: {}", threadId);
        return test;
    }

    public static synchronized ExtentTest getTest() {
        long threadId = Thread.currentThread().getId();
        ExtentTest test = extentTestMap.get(threadId);

        if (test != null) {
            logger.debug("Retrieved ExtentTest for thread ID: {}", threadId);
        } else {
            logger.warn("No ExtentTest found for thread ID: {}", threadId);
        }

        return test;
    }
}
