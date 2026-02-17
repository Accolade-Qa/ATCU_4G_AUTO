package com.aepl.atcu.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.atcu.base.TestBase;
import com.aepl.atcu.pages.DealearFotaPage;
import com.aepl.atcu.util.ExcelUtility;

public class DealerFotaPageTest extends TestBase {

	private DealearFotaPage page;
	private ExcelUtility excel;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.page = new DealearFotaPage(driver);
		this.excel = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excel, softAssert);
		this.excel.initializeExcel("Dealer_FOTA_Test");
		logger.info("Setup completed for DealerFotaPageTest");
	}

	@Test(priority = 1)
	public void testSessionIsActive() {
		executor.executeTest("Validate active logged-in session", true,
				() -> !driver.getCurrentUrl().toLowerCase().contains("/login"));
	}

	@Test(priority = 2)
	public void testClickNavBar() {
		executor.executeTest("Click Dealer FOTA from navigation", true, () -> {
			page.clickNavBar();
			return driver.getCurrentUrl().toLowerCase().contains("dealer-fota");
		});
	}

	@Test(priority = 3)
	public void testCheckPagination() {
		executor.executeTest("Validate pagination on Dealer FOTA page", true, () -> {
			page.checkPagination();
			return true;
		});
	}

	@Test(priority = 4)
	public void testClickSearchAndTable() {
		executor.executeTest("Validate search and table headings", true, () -> {
			page.clickSearchAndTable();
			return true;
		});
	}

	@Test(priority = 5)
	public void testClickAddApprovedFileButton() {
		executor.executeTest("Validate Add Approved File button flow", true, () -> {
			page.clickAddApprovedFileButton();
			return driver.getCurrentUrl().toLowerCase().contains("dealer-fota");
		});
	}

	@Test(priority = 6)
	public void testAddNewFileAndValidate() {
		executor.executeTest("Validate add new approved file", true,
				() -> page.addNewFileAndValidate("File uploaded successfully"));
	}

	@Test(priority = 7)
	public void testSearchBtnAndTableHeadings() {
		executor.executeTest("Validate search button and table headings", true, page::searchBtnAndTableHeadings);
	}

//	@Test(priority = 8)
	public void testDeleteActionBtn() {
		executor.executeTest("Validate delete action button alert", true, () -> {
			String msg = page.deleteActionBtn();
			return msg != null && !msg.trim().isEmpty();
		});
	}
}
