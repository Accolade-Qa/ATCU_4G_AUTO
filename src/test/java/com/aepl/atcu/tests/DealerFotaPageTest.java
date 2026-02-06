package com.aepl.atcu.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.aepl.atcu.base.TestBase;
import com.aepl.atcu.pages.CommonMethods;
import com.aepl.atcu.pages.DealearFotaPage;
import com.aepl.atcu.pages.LoginPage;
import com.aepl.atcu.util.ConfigProperties;
import com.aepl.atcu.util.ExcelUtility;

public class DealerFotaPageTest extends TestBase {
	private LoginPage loginPage;
	private ExcelUtility excelUtility;
	private DealearFotaPage dealerFota;
	private CommonMethods commonMethod;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.loginPage = new LoginPage(driver, null);
		this.dealerFota = new DealearFotaPage(driver);
		this.excelUtility = new ExcelUtility();
		this.commonMethod = new CommonMethods(driver, null);
		excelUtility.initializeExcel("Dealer_FOTA_Test");
	}

	@Test(priority = 1)
	public void login() {
		loginPage.enterUsername(ConfigProperties.getProperty("valid.username"))
				.enterPassword(ConfigProperties.getProperty("valid.password")).clickLogin();
	}

	@Test(priority = 2)
	public void testClickNavBar() {
		logger.info("Executing the 'Dealer FOTA PAGE'");
		String testCaseName = "Clicking on the dealer fota page";
		String expectedResult = ConfigProperties.getProperty("dealer.fota");
		String actualResult = "";
		String result;
		try {
			logger.info("Trying to click on the nav bar links");
			dealerFota.clickNavBar();
			actualResult = driver.getCurrentUrl();
			result = expectedResult.equalsIgnoreCase(actualResult) ? "PASS" : "FAIL";
			logger.info("Result is : " + result);
			excelUtility.writeTestDataToExcel(testCaseName, expectedResult, actualResult, result);
		} catch (Exception e) {
			logger.warn("Error");
			commonMethod.captureScreenshot(testCaseName);
			actualResult = driver.getCurrentUrl();
			result = expectedResult.equalsIgnoreCase(actualResult) ? "PASS" : "FAIL";
			excelUtility.writeTestDataToExcel(testCaseName, expectedResult, actualResult, result);
		}
	}

	@Test(priority = 3)
	public void testCheckPagination() {
	    logger.info("Testing pagination functionality.");
	    String testCaseName = "Pagination Test";
	    String expectedMessage = "Pagination validated successfully.";
	    String actualMessage = "";
	    String result = "";

	    try {
	        dealerFota.checkPagination();
	        actualMessage = expectedMessage;
	        result = "Pass";
	        logger.info("Test case passed: " + testCaseName);
	    } catch (Exception e) {
	        actualMessage = e.getMessage();
	        result = "Fail";
	        logger.error("Test case failed: " + testCaseName, e);
	    } finally {
	        excelUtility.writeTestDataToExcel(testCaseName, expectedMessage, actualMessage, result);
	    }
	}


	@Test(priority = 4)
	public void testClickSearchAndTable() {
		String testCaseName = "Test Search Box and Table Headings";
		String expectedMessage = "Search box and table headings validated successfully.";
		String actualMessage = "";
		String result = "";

		try {
			logger.info("Executing test case: " + testCaseName);

			dealerFota.clickSearchAndTable();

			actualMessage = expectedMessage;
			result = "Pass";
			logger.info("Test case passed: " + testCaseName);
		} catch (Exception e) {
			commonMethod.captureScreenshot(testCaseName);
			actualMessage = e.getMessage();
			result = "Fail";
			logger.error("Test case failed: " + testCaseName, e);
		} finally {
			excelUtility.writeTestDataToExcel(testCaseName, expectedMessage, actualMessage, result);
		}
	}

	@Test(priority = 5)
	public void testclickAddApprovedFileButton() {
		logger.info("Testing the app approved file button is working");
		String testCaseName = "Test Add Approved File Button";
		String expectedResult = ConfigProperties.getProperty("upload.csv");
		String actualResult = "";
		String result = "";

		try {
			dealerFota.clickAddApprovedFileButton();
			actualResult = driver.getCurrentUrl();
			result = expectedResult.equalsIgnoreCase(actualResult) ? "PASS" : "FAIL";
			logger.info("Result is : " + result);
			excelUtility.writeTestDataToExcel(testCaseName, expectedResult, actualResult, result);
		} catch (Exception e) {
			commonMethod.captureScreenshot(testCaseName);
			logger.warn("Error in the Dealer Fota Page - Add Approved File");
			actualResult = driver.getCurrentUrl();
			result = expectedResult.equalsIgnoreCase(actualResult) ? "PASS" : "FAIL";
			excelUtility.writeTestDataToExcel(testCaseName, expectedResult, actualResult, result);
		}
	}

	@Test(priority = 6)
	public void testAddNewFileAndValidate() {
		logger.info("Testing the add new file feature");

		String testCaseName = "Testing new file added and validating it";
		String expectedToastMessage = "File uploaded successfully";
		String actualMessage = "";
		String result = "";

		try {
			boolean isFileValidated = dealerFota.addNewFileAndValidate(expectedToastMessage);
			System.out.println(isFileValidated);

			if (isFileValidated) {
				result = "Pass";
				actualMessage = "File uploaded and validated successfully";
				logger.info("Test case passed: " + testCaseName);
			} else {
				result = "Fail";
				actualMessage = "File validation failed";
				logger.error("Test case failed: " + testCaseName);
			}
		} catch (Exception e) {
			commonMethod.captureScreenshot(testCaseName);
			result = "Fail";
			actualMessage = e.getMessage();
			logger.error("Exception occurred during test execution: " + testCaseName, e);
		} finally {
			excelUtility.writeTestDataToExcel(testCaseName, expectedToastMessage, actualMessage, result);
		}
	}

	@Test(priority = 7)
	public void testSearchBtnAndTableHeadings() {
		logger.info("Testing the search button and validating table headings");
		String testCaseName = "Testing search box and validate table headings";
		String expectedMessage = "Table heading matched";
		String actualMessage = "";
		String result = "";

		try {
			boolean isValidated = dealerFota.searchBtnAndTableHeadings();

			if (isValidated) {
				actualMessage = "Search button and table headings validated successfully";
				result = "Pass";
				logger.info("Test case passed: " + testCaseName);
			} else {
				actualMessage = "Search button or table headings validation failed";
				result = "Fail";
				logger.error("Test case failed: " + testCaseName);
			}
		} catch (Exception e) {
			commonMethod.captureScreenshot(testCaseName);
			actualMessage = e.getMessage();
			result = "Fail";
			logger.error("Exception occurred during test execution: " + testCaseName, e);
		} finally {
			excelUtility.writeTestDataToExcel(testCaseName, expectedMessage, actualMessage, result);
		}
	}

	@Test(priority = 8)
	public void testdeleteActionBtn() {
		String testCaseName = "Test Delete Action Button";
		String expectedMessage = "Clicked on the delete button";
		String actualMessage = "";
		String result = "";
		try {
			String msg = dealerFota.deleteActionBtn();
			actualMessage = msg;
			result = "PASS";
		} catch (Exception e) {
			actualMessage = e.getMessage();
			result = "FAIL";
			commonMethod.captureScreenshot(testCaseName);
		} finally {
			excelUtility.writeTestDataToExcel(testCaseName, expectedMessage, actualMessage, result);
		}
	}
}
