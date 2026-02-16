package com.aepl.atcu.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.atcu.base.TestBase;
import com.aepl.atcu.pages.DeviceDashboardPage;
import com.aepl.atcu.util.ExcelUtility;
import com.aepl.atcu.util.PageAssertionsUtil;

public class DeviceDashboardPageTest extends TestBase {

	private DeviceDashboardPage page;
	private PageAssertionsUtil common;
	private ExcelUtility excel;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.page = new DeviceDashboardPage(driver, wait, action);
		this.common = new PageAssertionsUtil(driver, wait);
		this.softAssert = new SoftAssert();
		this.excel = new ExcelUtility();
		this.executor = new Executor(excel, softAssert);
		this.excel.initializeExcel("Device_Dashboard_Page_Test");
		logger.info("Setup completed for DeviceDashboardPageTest");
	}

	// Verify Company Logo
	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Test page logo", true, common::verifyWebpageLogo);
	}

	// Verify Page Title
	@Test(priority = 2)
	public void testPageTitle() {
		executor.executeTest("Verify page title", "AEPL TCU4G QA Diagnostic Cloud", common::verifyPageTitle);
	}

	@Test(priority = 3)
	public void testClickNavBar() {
		executor.executeTest("Test nav bar click", "Link is verified", page::clickNavBar);
	}

	// Validate All Dashboard Components
	@Test(priority = 4)
	public void testValidateComponents() {
		executor.executeTest("Test all componenets", "All components are displayed and validated successfully.",
				common::validateComponents);
	}

	// Validate All Buttons
	@Test(priority = 5)
	public void testValidateButtons() {
		executor.executeTest("Validate All buttons are visible on the ui",
				"All buttons are displayed and enabled successfully.", common::validateButtons);
	}

	// Validate All Cards Visible
	@Test(priority = 6)
	public void testValidateAllCards() {
		executor.executeTest("Test all cards are visible or not", true, page::validateCardAreVisible);
	}

	// Verify Card 1 Visible
	@Test(priority = 7)
	public void testCard1Visible() {
		executor.executeTest("Verify Card 1 (Total Production Devices) visible", true,
				() -> page.validateCard1IsVisible());
	}

	// Verify Card 1 Title
	@Test(priority = 8)
	public void testCard1Title() {
		executor.executeTest("Verify Card 1 title", true,
				() -> page.verifyAndClickKPITotalProDevWithCount().contains("TOTAL PRODUCTION DEVICES"));
	}

	// Verify Card 1 Count Visible
	@Test(priority = 9)
	public void testCard1CountVisible() {
		executor.executeTest("Verify Card 1 count visible", true,
				() -> page.verifyAndClickKPITotalProDevWithCount().matches(".*KPI Count: \\d+.*"));
	}

	// Verify Card 1 Count Format (Numeric)
	@Test(priority = 10)
	public void testCard1CountFormat() {
		executor.executeTest("Verify Card 1 count is numeric format", true,
				() -> page.verifyAndClickKPITotalProDevWithCount().matches(".*KPI Count: \\d+.*"));
	}

	// Verify Card 1 Clickable
	@Test(priority = 11)
	public void testCard1Clickable() {
		executor.executeTest("Verify Card 1 clickable", true,
				() -> page.verifyAndClickKPITotalProDevWithCount().contains("KPI Table"));
	}

	// Verify Card 1 Table Visible
	@Test(priority = 12)
	public void testCard1TableVisible() {
		executor.executeTest("Verify Card 1 table visible after click", true,
				() -> !page.validateTotalProductionDevicesTableHeaders().isEmpty());
	}

	// Verify Card 1 Table Headers
	@Test(priority = 13)
	public void testCard1TableHeaders() {
		executor.executeTest("Verify Card 1 table headers", true,
				() -> !page.validateTotalProductionDevicesTableHeaders().isEmpty());
	}

	// Verify Card 1 Table Buttons
	@Test(priority = 14)
	public void testCard1TableButtons() {
		executor.executeTest("Verify Card 1 table action buttons enabled", true,
				page::validateTotalProductionDevicesTableButtons);
	}

	// Verify Card 1 Table Data Visible
	@Test(priority = 15)
	public void testCard1TableDataVisible() {
		executor.executeTest("Verify Card 1 table data visible", true,
				() -> page.validateTotalProductionDevicesTableHeaders().split(" ").length > 0);
	}

	// Verify Action Button Visible
	@Test(priority = 16)
	public void testActionButtonVisible() {
		executor.executeTest("Validate the action buttons visible", true, () -> page.validateActionButtonVisibility());
	}

	// Verify Action Button Enabled
	@Test(priority = 17)
	public void testActionButtonsEnabled() {
		executor.executeTest("Validate the action buttons enabled", true, () -> page.validateActionButtonsEnabled());
	}

	// Verify No Data Message (if empty)
	@Test(priority = 18)
	public void testNoDataMessageImg() {
		executor.executeTest("Validate the table with no data image", false, () -> page.ValidateNoDataImage());
	}

	// Verify Download Button Visible
	@Test(priority = 19)
	public void testDownloadButtonVisible() {
		executor.executeTest("Validate the download button is visible on table", true,
				() -> page.validateTheDownloadButtonIsVisibleOnTotalProductionDevicesTable());
	}

	// Verify Download Button Enabled
	@Test(priority = 20)
	public void testDownloadButtonEnable() {
		executor.executeTest("Validate the download button is enable on table", true,
				() -> page.validateTheDownloadButtonIsEnabledOnTotalProductionDevicesTable());
	}

	// Verify Download Button Clickable
	@Test(priority = 21)
	public void testDownloadButtonClickable() {
		executor.executeTest("Validate the download button is cliaclable on table", true,
				() -> page.validateTheDownloadButtonIsClickableOnTotalProductionDevicesTable());
	}

	// Verify Search Input Visible
	@Test(priority = 22)
	public void testSearchInputVisible() {
		executor.executeTest("Validate the search input is visible on table", true, page::validateSearchInputVisible);
	}

	// Verify Search Input Enabled
	@Test(priority = 23)
	public void testSearchInputEnabled() {
		executor.executeTest("Validate the search input is enabled on table", true, page::validateSearchInputEnabled);
	}

	// Verify Search Button Visible
	@Test(priority = 24)
	public void testSearchButtonVisible() {
		executor.executeTest("Validate the search button is visible on table", true, page::validateSearchButtonVisible);
	}

	// Verify Search Button Enabled
	@Test(priority = 25)
	public void testSearchButtonEnabled() {
		executor.executeTest("Validate the search button is enabled on table", true, page::validateSearchButtonEnabled);
	}

	// Verify Search With Valid Data
	@Test(priority = 26)
	public void testSearchWithValidData() {
		executor.executeTest("Validate the search with valid data on table", true, page::validateSearchWithValidData);
	}

	// Verify Search With Invalid Data
	@Test(priority = 27)
	public void testSearchWithInvalidData() {
		executor.executeTest("Validate the search with invalid data on table", true,
				page::validateSearchWithInvalidData);
	}

	// Verify Search Reset Behavior
	@Test(priority = 28)
	public void testSearchResetBehavior() {
		executor.executeTest("Validate the search reset behavior on table", true, page::validateSearchResetBehavior);
	}

	// Verify Pagination Visible
	@Test(priority = 29)
	public void testPaginationVisible() {
		executor.executeTest("Validate pagination is visible on table", true, page::validatePaginationVisible);
	}

	// Verify Next Page Working
	@Test(priority = 30)
	public void testNextPageWorking() {
		executor.executeTest("Validate next page working on table", true, page::validateNextPageWorking);
	}

	// Verify Previous Page Working
	@Test(priority = 31)
	public void testPreviousPageWorking() {
		executor.executeTest("Validate previous page working on table", true, page::validatePreviousPageWorking);
	}

	// Verify Page Number Click
	@Test(priority = 32)
	public void testPageNumberClick() {
		executor.executeTest("Validate page number click on table", true, page::validatePageNumberClick);
	}

	// Verify Pagination Hidden When No Data
	@Test(priority = 33)
	public void testPaginationHiddenWhenNoData() {
		executor.executeTest("Validate pagination hidden when table has no data", true,
				page::validatePaginationHiddenWhenNoData);
	}

	// Verify Card 2 Visible
	@Test(priority = 34)
	public void testCard2Visible() {
		executor.executeTest("Verify Card 2 visible", true, page::validateCard2IsVisible);
	}

	// Verify Card 2 Count
	@Test(priority = 35)
	public void testCard2Count() {
		executor.executeTest("Verify Card 2 count format", true, page::validateCard2CountFormat);
	}

	// Verify Card 2 Click
	@Test(priority = 36)
	public void testCard2Click() {
		executor.executeTest("Verify Card 2 clickable", true, page::validateCard2Clickable);
	}

	// Verify Table Visible
	@Test(priority = 37)
	public void testCard2TableVisible() {
		executor.executeTest("Verify Card 2 table visible after click", true, page::validateCard2TableVisible);
	}

	// Verify Headers
	@Test(priority = 38)
	public void testCard2TableHeaders() {
		executor.executeTest("Verify Card 2 table headers", true, page::validateCard2TableHeaders);
	}

	// Verify Buttons
	@Test(priority = 39)
	public void testCard2TableButtons() {
		executor.executeTest("Verify Card 2 table action buttons", true, page::validateCard2TableButtons);
	}

	// Verify Row Data
	@Test(priority = 40)
	public void testCard2RowDataVisible() {
		executor.executeTest("Verify Card 2 table row data visibility", true, page::validateCard2TableDataVisible);
	}

	// Verify Download Visible
	@Test(priority = 41)
	public void testCard2DownloadVisible() {
		executor.executeTest("Verify Card 2 download button visible", true, page::validateCard2DownloadButtonVisible);
	}

	// Verify Download Enabled

	// Verify Download Click

	// Verify Search Visible

	// Verify Search Enabled

	// Verify Valid Search

	// Verify Invalid Search

	// Verify Pagination

	// Verify Card 3 Visible

	// Verify Card 3 Count

	// Verify Card 3 Click

	// Verify Table Visible

	// Verify Headers

	// Verify Buttons

	// Verify Row Data

	// Verify Download Visible

	// Verify Download Enabled

	// Verify Download Click

	// Verify Search Visible

	// Verify Search Enabled

	// Verify Valid Search

	// Verify Invalid Search

	// Verify Pagination

	// Verify Card 4 Visible

	// Verify Card 4 Count

	// Verify Card 4 Click

	// Verify Table Visible

	// Verify Headers

	// Verify Buttons

	// Verify Row Data

	// Verify Download Visible

	// Verify Download Enabled

	// Verify Download Click

	// Verify Search Visible

	// Verify Search Enabled

	// Verify Valid Search

	// Verify Invalid Search

	// Verify Pagination

	// Verify Graph 1 Visible

	// Verify Graph 1 Title

	// Verify Graph Legend Visible

	// Verify Graph Tooltip Visible

	// Verify Graph Clickable

	// Verify Graph Click Opens Correct Table

	// Verify Graph Table Visible

	// Verify Graph Table Headers

	// Verify Graph Table Data Visible

	// Verify Graph Table Action Buttons

	// Verify Download Visible

	// Verify Download Enabled

	// Verify Download Click

	// Verify Search Visible

	// Verify Search Enabled

	// Verify Valid Search

	// Verify Invalid Search

	// Verify Pagination Visible

	// Verify Next Page

	// Verify Previous Page

	// Verify Graph 2 Visible

	// Verify Graph 2 Title

	// Verify Graph Legend Visible

	// Verify Graph Tooltip Visible

	// Verify Graph Clickable

	// Verify Graph Click Opens Correct Table

	// Verify Graph Table Visible

	// Verify Graph Table Headers

	// Verify Graph Table Data Visible

	// Verify Graph Table Action Buttons

	// Verify Download Visible

	// Verify Download Enabled

	// Verify Download Click

	// Verify Search Visible

	// Verify Search Enabled

	// Verify Valid Search

	// Verify Invalid Search

	// Verify Pagination Visible

	// Verify Next Page

	// Verify Previous Page

	// Verify Graph 3 Visible

	// Verify Graph 3 Title

	// Verify Graph Legend Visible

	// Verify Graph Tooltip Visible

	// Verify Graph Clickable

	// Verify Graph Click Opens Correct Table

	// Verify Graph Table Visible

	// Verify Graph Table Headers

	// Verify Graph Table Data Visible

	// Verify Graph Table Action Buttons

	// Verify Download Visible

	// Verify Download Enabled

	// Verify Download Click

	// Verify Search Visible

	// Verify Search Enabled

	// Verify Valid Search

	// Verify Invalid Search

	// Verify Pagination Visible

	// Verify Next Page

	// Verify Previous Page

	// Verify Footer Visible

	// Verify Version Text

	// Verify Copyright

	// Verify Footer Alignment

}
