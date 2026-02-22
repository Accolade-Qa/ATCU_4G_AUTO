package com.aepl.atcu.tests;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.atcu.base.TestBase;
import com.aepl.atcu.pages.DeviceDashboardPage;
import com.aepl.atcu.util.ExcelUtility;
import com.aepl.atcu.util.PageAssertionsUtil;

public class DeviceDashboardPageTest extends TestBase {
	private DeviceDashboardPage page;
	private PageAssertionsUtil assertions;
	private ExcelUtility excel;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.page = new DeviceDashboardPage(driver, wait, action);
		this.assertions = new PageAssertionsUtil(driver, wait);
		this.softAssert = new SoftAssert();
		this.excel = new ExcelUtility();
		this.executor = new Executor(excel, softAssert);
		this.excel.initializeExcel("Device_Dashboard_Page_Test");
		logger.info("Setup completed for DeviceDashboardPageTest.");
	}

	@Test(priority = 1)
	public void testCompanyLogo() {
		executor.executeTest("Verify Company Logo", true, assertions::verifyWebpageLogo);
	}

	@Test(priority = 2)
	public void testPageTitle() {
		final String expTitle = "AEPL TCU4G QA Diagnostic Cloud";
		executor.executeTest("Verify Page Title", expTitle, assertions::verifyPageTitle);
	}

	@Test(priority = 3)
	public void testClickNavBar() {
		final String expNav = "Link is verified";
		executor.executeTest("Verify Navigation Bar Link", expNav, page::clickNavBar);
	}

	@Test(priority = 4)
	public void testValidateComponents() {
		final String expComponents = "All components are displayed and validated successfully.";
		executor.executeTest("Validate Components", expComponents, assertions::validateComponents);
	}

	@Test(priority = 5)
	public void testValidateButtons() {
		final String expButtons = "All buttons are displayed and enabled successfully.";
		executor.executeTest("Validate Buttons", expButtons, assertions::validateButtons);
	}

	@Test(priority = 6)
	public void testCardsVisible() {
		executor.executeTest("Validate KPI Cards Visibility", true, page::validateCardAreVisible);
	}

	@Test(priority = 10)
	public void testCard1Headers() {
		final List<String> headersProduction = List.of("UIN NO.", "IMEI NO.", "ICCID NO.", "MODEL NAME.",
				"ACTION");
		executor.executeTest("Total Production Devices Headers", headersProduction,
				() -> page.getTableHeadersForKpiCard(1));
	}

	@Test(priority = 11)
	public void testCard1TableButtons() {
		executor.executeTest("Total Production Devices Table Buttons", true, page::validateTotalProductionDevicesTableButtons);
	}

	@Test(priority = 12)
	public void testCard1SearchValidData() {
		executor.executeTest("Total Production Devices Search With Saved Data", true, () -> {
			page.openKpiCardAndGetComponentTitle(1);
			return page.validateSearchWithValidData();
		});
	}

	@Test(priority = 13)
	public void testCard1SearchInvalidData() {
		executor.executeTest("Total Production Devices Search With Invalid Data", true, () -> {
			page.openKpiCardAndGetComponentTitle(1);
			return page.validateSearchWithInvalidData();
		});
	}

	@Test(priority = 14)
	public void testCard1Pagination() {
		executor.executeTest("Total Production Devices Pagination Next/Previous", true, () -> {
			page.openKpiCardAndGetComponentTitle(1);
			return page.validatePaginationVisible() && page.validateNextPageWorking() && page.validatePreviousPageWorking();
		});
	}

	@Test(priority = 20)
	public void testCard2Headers() {
		final List<String> headersDispatched = List.of("UIN NO.", "IMEI NO.", "ICCID NO.", "MODEL NAME.",
				"CUSTOMER NAME", "ACTION");
		executor.executeTest("Total Dispatched Devices Headers", headersDispatched,
				() -> page.getTableHeadersForKpiCard(2));
	}

	@Test(priority = 21)
	public void testCard2TableButtons() {
		executor.executeTest("Total Dispatched Devices Table Buttons", true, page::validateCard2TableButtons);
	}

	@Test(priority = 22)
	public void testCard2SearchValidData() {
		executor.executeTest("Total Dispatched Devices Search With Saved Data", true, () -> {
			page.openKpiCardAndGetComponentTitle(2);
			return page.validateSearchWithValidData();
		});
	}

	@Test(priority = 23)
	public void testCard2SearchInvalidData() {
		executor.executeTest("Total Dispatched Devices Search With Invalid Data", true, () -> {
			page.openKpiCardAndGetComponentTitle(2);
			return page.validateSearchWithInvalidData();
		});
	}

	@Test(priority = 24)
	public void testCard2Pagination() {
		executor.executeTest("Total Dispatched Devices Pagination Next/Previous", true, () -> {
			page.openKpiCardAndGetComponentTitle(2);
			return page.validatePaginationVisible() && page.validateNextPageWorking() && page.validatePreviousPageWorking();
		});
	}

	@Test(priority = 30)
	public void testCard3Headers() {
		final List<String> headersInstalled = List.of("UIN NO.", "IMEI NO.", "ICCID NO.", "CHASSIS NO.",
				"MODEL NAME.", "CUSTOMER NAME", "ACTION");
		executor.executeTest("Total Installed Devices Headers", headersInstalled,
				() -> page.getTableHeadersForKpiCard(3));
	}

	@Test(priority = 31)
	public void testCard3TableButtons() {
		executor.executeTest("Total Installed Devices Table Buttons", true, page::validateCard3TableButtons);
	}

	@Test(priority = 32)
	public void testCard3SearchValidData() {
		executor.executeTest("Total Installed Devices Search With Saved Data", true, () -> {
			page.openKpiCardAndGetComponentTitle(3);
			return page.validateSearchWithValidData();
		});
	}

	@Test(priority = 33)
	public void testCard3SearchInvalidData() {
		executor.executeTest("Total Installed Devices Search With Invalid Data", true, () -> {
			page.openKpiCardAndGetComponentTitle(3);
			return page.validateSearchWithInvalidData();
		});
	}

	@Test(priority = 34)
	public void testCard3Pagination() {
		executor.executeTest("Total Installed Devices Pagination Next/Previous", true, () -> {
			page.openKpiCardAndGetComponentTitle(3);
			return page.validatePaginationVisible() && page.validateNextPageWorking() && page.validatePreviousPageWorking();
		});
	}

	@Test(priority = 40)
	public void testCard4Headers() {
		final List<String> headersDiscarded = List.of("UIN NO.", "IMEI NO.", "ICCID NO.", "CHASSIS NO.",
				"MODEL NAME.", "INSTALLED AT", "DISCARDED AT", "ACTION");
		executor.executeTest("Total Discarded Devices Headers", headersDiscarded,
				() -> page.getTableHeadersForKpiCard(4));
	}

	@Test(priority = 41)
	public void testCard4TableButtons() {
		executor.executeTest("Total Discarded Devices Table Buttons", true, page::validateCard4TableButtons);
	}

	@Test(priority = 42)
	public void testCard4SearchValidData() {
		executor.executeTest("Total Discarded Devices Search With Saved Data", true, () -> {
			page.openKpiCardAndGetComponentTitle(4);
			return page.validateSearchWithValidData();
		});
	}

	@Test(priority = 43)
	public void testCard4SearchInvalidData() {
		executor.executeTest("Total Discarded Devices Search With Invalid Data", true, () -> {
			page.openKpiCardAndGetComponentTitle(4);
			return page.validateSearchWithInvalidData();
		});
	}

	@Test(priority = 44)
	public void testCard4Pagination() {
		executor.executeTest("Total Discarded Devices Pagination Next/Previous", true, () -> {
			page.openKpiCardAndGetComponentTitle(4);
			return page.validatePaginationVisible() && page.validateNextPageWorking() && page.validatePreviousPageWorking();
		});
	}

	@Test(priority = 50)
	public void testGraph1Flow() {
		executor.executeTest("Graph1 End-To-End", true, () -> page.getTotalGraphCount() >= 1
				&& page.isGraphWidgetVisible(1)
				&& !page.getGraphTitle(1).isBlank()
				&& page.isGraphLegendVisible(1)
				&& page.isGraphClickable(1)
				&& page.hasGraphTableHeaders(1)
				&& page.hasGraphTableDataOrNoDataState(1));
	}

	@Test(priority = 51)
	public void testGraph1SearchValidData() {
		executor.executeTest("Graph1 Table Search With Saved Data", true, () -> page.getTotalGraphCount() >= 1
				&& page.validateGraphTableSearchWithValidData(1));
	}

	@Test(priority = 52)
	public void testGraph1SearchInvalidData() {
		executor.executeTest("Graph1 Table Search With Invalid Data", true, () -> page.getTotalGraphCount() >= 1
				&& ("NO_DATA".equals(page.getGraphTableInvalidSearchState(1))
						|| "HAS_DATA".equals(page.getGraphTableInvalidSearchState(1))));
	}

	@Test(priority = 53)
	public void testGraph1Pagination() {
		executor.executeTest("Graph1 Table Pagination Next/Previous", true, () -> page.getTotalGraphCount() >= 1
				&& page.validateGraphTableNextPagination(1)
				&& page.validateGraphTablePreviousPagination(1));
	}

	@Test(priority = 54)
	public void testGraph2Flow() {
		executor.executeTest("Graph2 End-To-End", true, () -> page.getTotalGraphCount() < 2 || (page.isGraphWidgetVisible(2)
				&& !page.getGraphTitle(2).isBlank()
				&& page.isGraphLegendVisible(2)
				&& page.isGraphClickable(2)
				&& page.hasGraphTableHeaders(2)
				&& page.hasGraphTableDataOrNoDataState(2)));
	}

	@Test(priority = 55)
	public void testGraph3Flow() {
		executor.executeTest("Graph3 End-To-End", true, () -> page.getTotalGraphCount() < 3 || (page.isGraphWidgetVisible(3)
				&& !page.getGraphTitle(3).isBlank()
				&& page.isGraphLegendVisible(3)
				&& page.isGraphClickable(3)
				&& page.hasGraphTableHeaders(3)
				&& page.hasGraphTableDataOrNoDataState(3)));
	}

	@Test(priority = 99)
	public void testVersion() {
		executor.executeTest("Verify Application Version", true, () -> !assertions.checkVersion().isBlank());
	}

	@Test(priority = 100)
	public void testCopyright() {
		executor.executeTest("Verify Copyright", true, () -> assertions.checkCopyright().contains("Accolade"));
	}

	@AfterClass
	public void tearDownAssertions() {
		softAssert.assertAll();
	}
}
