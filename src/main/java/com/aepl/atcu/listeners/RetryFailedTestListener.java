package com.aepl.atcu.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryFailedTestListener implements IRetryAnalyzer {

	private int retryCount = 0;
	private static final int maxRetryCount = 3;
	private static final Logger logger = LogManager.getLogger(RetryFailedTestListener.class);

	@Override
	public boolean retry(ITestResult result) {
		if (retryCount < maxRetryCount) {
			retryCount++;
			logger.info("Retrying test: {} | Attempt: {}", result.getName(), retryCount);
			return true;
		}
		return false;
	}
}
