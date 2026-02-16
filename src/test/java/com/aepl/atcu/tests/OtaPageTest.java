package com.aepl.atcu.tests;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.atcu.base.TestBase;
import com.aepl.atcu.pages.OtaPage;
import com.aepl.atcu.util.ConfigProperties;
import com.aepl.atcu.util.ExcelUtility;

public class OtaPageTest extends TestBase {

	private OtaPage otaPage;
	private ExcelUtility excel;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.otaPage = new OtaPage(driver);
		this.excel = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excel, softAssert);
		this.excel.initializeExcel("Ota_Page_Test");
		logger.info("Setup completed for OtaPageTest");
	}

	@Test(priority = 1)
	public void testSessionIsActive() {
		executor.executeTest("Validate active logged-in session", true,
				() -> !driver.getCurrentUrl().toLowerCase().contains("/login"));
	}

	@Test(priority = 2)
	public void testClickNavBar() {
		executor.executeTest("Click Device Utility from navigation", true, () -> {
			otaPage.clickNavBar();
			String expectedUrl = ConfigProperties.getProperty("dashboard.url");
			String currentUrl = driver.getCurrentUrl();
			return currentUrl != null && expectedUrl != null
					&& currentUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 3)
	public void testClickOnOta() {
		executor.executeTest("Open OTA page from dropdown", true, () -> {
			String expectedUrl = ConfigProperties.getProperty("ota");
			String actualUrl = otaPage.clickDropdown();
			return actualUrl != null && expectedUrl != null && actualUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 4)
	public void testCheckButtons() {
		executor.executeTest("Validate OTA page buttons", true, () -> {
			otaPage.checkButtons();
			return true;
		});
	}

	@Test(priority = 5)
	public void testCheckActionButtons() {
		executor.executeTest("Validate OTA table action buttons", true, () -> {
			otaPage.checkActionButtons();
			return true;
		});
	}

	@Test(priority = 6)
	public void testCheckSearchBoxAndTable() {
		executor.executeTest("Validate OTA search and table headers", true, () -> {
			String batchName = "SB_OTA_TEST";
			List<String> expectedHeaders = Arrays.asList("Batch ID", "Batch Name", "Batch Description", "Created By",
					"Created At", "Batch Breakdown", "Completed Percentage", "Batch Status", "Action");
			return otaPage.checkSearchBoxAndTable(batchName, expectedHeaders);
		});
	}

	@Test(priority = 7)
	public void testCheckPagination() {
		executor.executeTest("Validate OTA pagination", true, () -> {
			otaPage.checkPagination();
			return true;
		});
	}

	@Test(priority = 8)
	public void testClickOtaBatchReport() {
		executor.executeTest("Validate OTA Batch Report button", true, () -> {
			otaPage.clickOtaBatchReport();
			return true;
		});
	}

	@Test(priority = 9)
	public void testGetOtaBatchDateWise() {
		executor.executeTest("Validate OTA Batch Date Wise flow", true, () -> {
			otaPage.getOtaBatchDateWise();
			return true;
		});
	}

	@Test(priority = 10)
	public void testCheckTableHeading() {
		executor.executeTest("Validate OTA report table headings", true, () -> {
			otaPage.checktableHeading();
			return true;
		});
	}

	@Test(priority = 11)
	public void testCheckReportsButtons() {
		executor.executeTest("Validate OTA report download button", true, otaPage::checkReportsButtons);
	}

	@Test(priority = 12)
	public void testClickOtaMaster() {
		executor.executeTest("Open OTA Master page", true, () -> {
			String expectedUrl = ConfigProperties.getProperty("ota.master");
			String actualUrl = otaPage.clickOtaMaster();
			return actualUrl != null && expectedUrl != null && actualUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 13)
	public void testAddNewOta() {
		executor.executeTest("Validate Add New OTA form submission", true, () -> {
			String actualResult = otaPage.fillAndSubmitOtaForm("add");
			return actualResult != null && actualResult.toLowerCase().contains("success");
		});
	}

	@Test(priority = 14)
	public void testCheckSearchAndTableOfOtaMaster() {
		executor.executeTest("Validate OTA Master search and table headers", true,
				otaPage::checkSearchAndTableOfOtaMaster);
	}

	@Test(priority = 15)
	public void testCheckOtaMasterActionButtons() {
		executor.executeTest("Validate OTA Master action buttons", true, () -> {
			otaPage.checkOtaMasterActionButtons();
			return true;
		});
	}

	@Test(priority = 16)
	public void testCheckPaginationOfOtaMaster() {
		executor.executeTest("Validate OTA Master pagination", true, () -> {
			otaPage.checkPagination();
			return true;
		});
	}

	@Test(priority = 17)
	public void testSelectOtaTypeDropdown() {
		executor.executeTest("Validate OTA type dropdown selection", true, () -> {
			otaPage.selectOtaTypeDropdown();
			return true;
		});
	}
}
