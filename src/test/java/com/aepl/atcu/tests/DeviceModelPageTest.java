package com.aepl.atcu.tests;

import org.apache.logging.log4j.Level;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aepl.atcu.base.TestBase;
import com.aepl.atcu.pages.CommonMethods;
import com.aepl.atcu.pages.DeviceModelPage;
import com.aepl.atcu.pages.LoginPage;
import com.aepl.atcu.util.ConfigProperties;
import com.aepl.atcu.util.ExcelUtility;

public class DeviceModelPageTest extends TestBase {
	private LoginPage loginPage;
	private DeviceModelPage deviceModel;
	private ExcelUtility excelUtility;
	private CommonMethods commonMethod;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		deviceModel = new DeviceModelPage(driver);
		loginPage = new LoginPage(driver, null);
		excelUtility = new ExcelUtility();
		excelUtility.initializeExcel("Device Model");
	}

	@Test(priority = 1)
	public void testLogin() {
		loginPage.enterUsername(ConfigProperties.getProperty("valid.username"))
				.enterPassword(ConfigProperties.getProperty("valid.password")).clickLogin();
	}

	@Test(priority = 2)
	public void testClickNavBar() {
		String testCaseName = "Test Navbar Links";
		String expectedURL = ConfigProperties.getProperty("dashboard.url");
		String actualURL = "";
		String result;
		logger.info("Executing the testClickNavBar");
		try {
			logger.info("Trying to click on the nav bar links");
			deviceModel.clickNavBar();
			actualURL = driver.getCurrentUrl();
			result = expectedURL.equalsIgnoreCase(actualURL) ? "PASS" : "FAIL";
			logger.info("Result is : " + result);
			excelUtility.writeTestDataToExcel(testCaseName, expectedURL, actualURL, result);
		} catch (Exception e) {
			logger.warn("Error");
			e.printStackTrace();
			actualURL = driver.getCurrentUrl();
			result = expectedURL.equalsIgnoreCase(actualURL) ? "PASS" : "FAIL";
			excelUtility.writeTestDataToExcel(testCaseName, expectedURL, actualURL, result);
		}
		logger.info("Successfully clicked on the Device Utility.");
	}

	@Test(priority = 3)
	public void testClickOnDeviceModel() {
		String testCaseName = "Test Device Model Link";
		String expectedURL = ConfigProperties.getProperty("device.model");
		String result = "";

		logger.info("Executing test case: {}", testCaseName);

		try {
			String actualURL = deviceModel.clickDeviceModel();
			result = expectedURL.equalsIgnoreCase(actualURL) ? "PASS" : "FAIL";
			logger.info("Test case '{}' completed successfully. Expected URL: {}, Actual URL: {}", testCaseName,
					expectedURL, actualURL);
		} catch (Exception e) {
			logger.error("Error encountered in test case '{}'.", testCaseName, e);
			result = "FAIL";
		} finally {
			excelUtility.writeTestDataToExcel(testCaseName, expectedURL,
					result.equals("PASS") ? expectedURL : "Error occurred", result);
		}
	}

	@Test(priority = 4)
	public void testClickAddDeviceModel() {
		String testCaseName = "Test Add Device Model Button";
		String expectedURL = ConfigProperties.getProperty("add.device.model");
		String result = "";

		logger.info("Executing test case: {}", testCaseName);

		try {
			String actualURL = deviceModel.clickAddDeviceModel();
			result = expectedURL.equalsIgnoreCase(actualURL) ? "PASS" : "FAIL";
			logger.info("Test case '{}' completed successfully. Expected URL: {}, Actual URL: {}", testCaseName,
					expectedURL, actualURL);
		} catch (Exception e) {
			logger.error("Error encountered in test case '{}'.", testCaseName, e);
			result = "FAIL";
		} finally {
			excelUtility.writeTestDataToExcel(testCaseName, expectedURL,
					result.equals("PASS") ? expectedURL : "Error occurred", result);
		}
	}

	@Test(priority = 5)
	public void testAddNewDeviceModel() {
		String testCaseName = "Test Add New Device Model";
		String expectedMessage = ConfigProperties.getProperty("device.model");
		String actualMessage = "";
		String result;

		logger.info("Executing the testAddNewDeviceModel");
		try {
			logger.info("Trying to add a new device model");
			deviceModel.AddNewDeviceModel();
			Thread.sleep(1000);
			actualMessage = driver.getCurrentUrl();

			result = expectedMessage.equalsIgnoreCase(actualMessage) ? "PASS" : "FAIL";
			logger.info("Result is : " + result);

			excelUtility.writeTestDataToExcel(testCaseName, expectedMessage, actualMessage, result);
		} catch (Exception e) {
			logger.warn("Error occurred while adding a new device model");
			e.printStackTrace();

			commonMethod.captureScreenshot(testCaseName);
			actualMessage = "Error"; // Adjust based on failure scenario

			result = "FAIL";
			excelUtility.writeTestDataToExcel(testCaseName, expectedMessage, actualMessage, result);
		}
		logger.info("Successfully executed testAddNewDeviceModel.");
	}

	@Test(priority = 6)
	public void testCheckSearchBoxAndTable() {
		String testCaseName = "Testing search box and table headers";
		String expectedResult = "Search box and the table is validate successfully";
		String actualResult = "";
		String result = "";
		boolean isChecked = false;

		try {
			isChecked = deviceModel.checkSearchBoxAndTable();
			actualResult = "Search box and the table is validate successfully";
			result = isChecked ? "PASS" : "FAIL";
		} catch (Exception e) {
			actualResult = "Issue in the search box and the table heading";
			result = isChecked ? "PASS" : "FAIL";
			commonMethod.captureScreenshot(testCaseName);
			logger.log(Level.INFO, "Facing issue in the search box and table heading of the {} ", testCaseName);
			e.getMessage();
		} finally {
			logger.log(Level.INFO, "Writing data to the excel file for testcase {}", testCaseName);
			excelUtility.writeTestDataToExcel(testCaseName, expectedResult, actualResult, result);
		}
	}

	@Test(priority = 7)
	public void testClickViewIcon() {
		String testCaseName = "Test View Icon Link";
		String expectedURL = ConfigProperties.getProperty("add.device.model");
		String result = "";

		logger.info("Executing test case: {}", testCaseName);

		try {
			String actualURL = deviceModel.clickViewIcon();
			System.out.println("Actual URL : " + actualURL);

			result = actualURL.contains(expectedURL) ? "PASS" : "FAIL";
			logger.info("Test case '{}' completed successfully. Expected URL: {}, Actual URL: {}", testCaseName,
					expectedURL, actualURL);
		} catch (Exception e) {
			logger.error("Error encountered in test case '{}'.", testCaseName, e);
			result = "FAIL";
		} finally {
			excelUtility.writeTestDataToExcel(testCaseName, expectedURL,
					result.equals("PASS") ? expectedURL : "Error occurred", result);
		}

	}

	@Test(priority = 8)
	public void testUpdateDeviceModel() {
		String testCaseName = "Test Update Device Model";
		String expectedMessage = ConfigProperties.getProperty("device.model");
		String actualMessage = "";
		String result;

		logger.info("Executing the testUpdateDeviceModel");
		try {
			logger.info("Trying to Update device model");
			deviceModel.UpdateDeviceModel();
			Thread.sleep(1000);
			actualMessage = driver.getCurrentUrl();

			result = expectedMessage.equalsIgnoreCase(actualMessage) ? "PASS" : "FAIL";
			logger.info("Result is : " + result);

			excelUtility.writeTestDataToExcel(testCaseName, expectedMessage, actualMessage, result);
		} catch (Exception e) {
			logger.warn("Error occurred while adding a new device model");
			e.printStackTrace();

			commonMethod.captureScreenshot(testCaseName);
			actualMessage = "Error"; // Adjust based on failure scenario

			result = "FAIL";
			excelUtility.writeTestDataToExcel(testCaseName, expectedMessage, actualMessage, result);
		}
		logger.info("Successfully executed testAddNewDeviceModel.");
	}

	@Test(priority = 9)
	public void testCheckSearchBoxAndTable2() {
		String testCaseName = "Testing search box and table headers";
		String expectedResult = "Search box and the table is validate successfully";
		String actualResult = "";
		String result = "";
		boolean isChecked = false;

		try {
			isChecked = deviceModel.checkSearchBoxAndTable2();
			actualResult = "Search box and the table is validate successfully";
			result = isChecked ? "PASS" : "FAIL";
		} catch (Exception e) {
			actualResult = "Issue in the search box and the table heading";
			result = isChecked ? "PASS" : "FAIL";
			commonMethod.captureScreenshot(testCaseName);
			logger.log(Level.INFO, "Facing issue in the search box and table heading of the {} ", testCaseName);
			e.getMessage();
		} finally {
			logger.log(Level.INFO, "Writing data to the excel file for testcase {}", testCaseName);
			excelUtility.writeTestDataToExcel(testCaseName, expectedResult, actualResult, result);
		}
	}

	@Test(priority = 10)
	public void testClickDeleteIcon() {
		String testCaseName = "Test Delete Icon Link";
		String expectedURL = ConfigProperties.getProperty("add.device.model");
		String result = "";

		logger.info("Executing test case: {}", testCaseName);

		try {
			boolean actualURL = deviceModel.clickDeleteIcon();
			System.out.println("Actual URL : " + actualURL);

//			result = actualURL.contains(expectedURL) ? "PASS" : "FAIL";
			logger.info("Test case '{}' completed successfully. Expected URL: {}, Actual URL: {}", testCaseName,
					expectedURL, actualURL);
		} catch (Exception e) {
			logger.error("Error encountered in test case '{}'.", testCaseName, e);
			result = "FAIL";
		} finally {
			excelUtility.writeTestDataToExcel(testCaseName, expectedURL,
					result.equals("PASS") ? expectedURL : "Error occurred", result);
		}

	}

	@Test(priority = 11)
	public void testCheckPagination() {
		String testCaseName = "Testing the pagination";
		String expectedResult = "Pagination should work correctly by navigating through pages";
		String actualResult = "";
		String result = "Fail";
		logger.info("Executing test case: {}", testCaseName);

		try {
			deviceModel.checkPagination();
			actualResult = "Pagination worked as expected";
			result = "Pass";
		} catch (Exception e) {
			actualResult = "Exception occurred: " + e.getMessage();
			commonMethod.captureScreenshot(testCaseName);
			logger.error("Error during pagination test: ", e);
		} finally {
			logger.info("Writing test results to Excel for {}", testCaseName);
			excelUtility.writeTestDataToExcel(testCaseName, expectedResult, actualResult, result);
		}
	}

}
