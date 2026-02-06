package com.aepl.atcu.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aepl.atcu.base.TestBase;
import com.aepl.atcu.pages.DeviceDashboardPage;
import com.aepl.atcu.pages.LoginPage;
import com.aepl.atcu.util.ConfigProperties;
import com.aepl.atcu.util.ExcelUtility;

public class DeviceDashboardPageTest extends TestBase {

	private LoginPage loginPage;
	private ExcelUtility excelUtility;
	private DeviceDashboardPage DeviceDashboard;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		loginPage = new LoginPage(driver);
		DeviceDashboard = new DeviceDashboardPage(driver);
		excelUtility = new ExcelUtility();
		excelUtility.initializeExcel("Device_ashboard_Test");
	}

	@Test(priority = 1)
	public void testLogin() {
		String testCaseName = "Login Test In Device Dashboard";
		try {
			logger.info("Executing Login: " + testCaseName);
			loginPage.enterUsername(ConfigProperties.getProperty("valid.username"))
					.enterPassword(ConfigProperties.getProperty("valid.password")).clickLogin();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test(priority = 2)
	public void testKPICards() {
		String testCaseName = "Test KPI Cards";
		String expectedURL = ConfigProperties.getProperty("dashboard.url");
		String actualURL = "";
		String result;
		logger.info("checking the KPI Cards");

		try {
			logger.info("Trying to check kpi cards on dashboard");
			DeviceDashboard.CheckKPICard();
			actualURL = driver.getCurrentUrl();
			result = expectedURL.equalsIgnoreCase(actualURL) ? "PASS" : "FAIL";
			logger.info("Result is : " + result);
			excelUtility.writeTestDataToExcel(testCaseName, expectedURL, actualURL, result);
		} catch (Exception e) {
			logger.warn("Error");
			e.printStackTrace();
			actualURL = driver.getCurrentUrl();
			// captureScreenshot(testCaseName);
			result = expectedURL.equalsIgnoreCase(actualURL) ? "PASS" : "FAIL";
			excelUtility.writeTestDataToExcel(testCaseName, expectedURL, actualURL, result);
		}
		logger.info("Successfully checked kpi cards");

	}

	@Test(priority = 3)

	public void TestDeviceModelWise() {

		String testCaseName = "Test Device Model Wise buttons";
		String expectedURL = ConfigProperties.getProperty("dashboard.url");
		String result = "";

		logger.info("Executing test case: {}", testCaseName);

		try {
			String actualURL = DeviceDashboard.DeviceModelWise();
			result = expectedURL.equalsIgnoreCase(actualURL) ? "PASS" : "FAIL";
			logger.info("Test case '{}' completed successfully. Expected URL: {}, Actual URL: {}", testCaseName,
					expectedURL, actualURL);
		} catch (Exception e) {
			logger.error("Error encountered in test case '{}'.", testCaseName, e);
			// captureScreenshot(testCaseName);
			result = "FAIL";
		} finally {
			excelUtility.writeTestDataToExcel(testCaseName, expectedURL,
					result.equals("PASS") ? expectedURL : "Error occurred", result);
		}

	}

	@Test(priority = 4)
	public void testGraphEnability() {
		String testCaseName = "Testing the graph enability";

	}

}
