package com.aepl.atcu.tests;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.atcu.base.TestBase;
import com.aepl.atcu.pages.MyAis140TicketsPage;
import com.aepl.atcu.util.ConfigProperties;
import com.aepl.atcu.util.ExcelUtility;

public class MyAis140TicketsPageTest extends TestBase {

	private MyAis140TicketsPage page;
	private ExcelUtility excel;
	private SoftAssert softAssert;
	private Executor executor;
	private String apiTicketNo;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.page = new MyAis140TicketsPage(driver);
		this.excel = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excel, softAssert);
		this.excel.initializeExcel("MyAis140TicketsPage_Test");
		logger.info("Setup completed for MyAis140TicketsPageTest");
	}

	@Test(priority = 1)
	public void testSessionIsActive() {
		executor.executeTest("Validate active logged-in session", true,
				() -> !driver.getCurrentUrl().toLowerCase().contains("/login"));
	}

	@Test(priority = 2)
	public void testClickNavBar() {
		executor.executeTest("Click Device Utility from navigation", true, () -> {
			page.clickNavBar();
			String expectedUrl = ConfigProperties.getProperty("dashboard.url");
			String currentUrl = driver.getCurrentUrl();
			return expectedUrl != null && currentUrl != null
					&& currentUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 3)
	public void testOpenMyAis140TicketPage() {
		executor.executeTest("Open My AIS140 Ticket page", true, () -> {
			String currentUrl = page.clickDropDownOption();
			return currentUrl != null && currentUrl.toLowerCase().contains("ais140");
		});
	}

	@Test(priority = 4)
	public void testSearchBoxVisible() {
		executor.executeTest("Validate ticket search with API-generated ticket", true, () -> {
			WebElement searchBox = page.ClickSearchBox();
			if (searchBox == null) {
				return false;
			}
			if (apiTicketNo == null || apiTicketNo.isBlank()) {
				apiTicketNo = page.generateAisTicketFromApi();
			}
			String searchedValue = page.searchByTicketNo(apiTicketNo);
			return searchedValue != null && searchedValue.contains(apiTicketNo);
		});
	}

	@Test(priority = 5)
	public void testClickViewButton() {
		executor.executeTest("Open first ticket details", true, () -> {
			page.ClickViewButton();
			return true;
		});
	}

	@Test(priority = 6)
	public void testTicketInformationSection() {
		executor.executeTest("Open ticket information section", true, () -> {
			page.ClickTicketInformation();
			return true;
		});
	}

	@Test(priority = 7)
	public void testTicketInformationFields() {
		executor.executeTest("Validate ticket information fields interaction", true, () -> {
			page.TicketNumber();
			page.TicketCretedTime();
			page.TicketAssignedTime();
			page.TicketCancelledCompletedTime();
			page.TicketCertificateValidityDuration();
			page.TicketOverAllStatus();
			page.TicketOverAllRemark();
			page.TicketGeneratedBy();
			page.TicketDescription();
			return true;
		});
	}

	@Test(priority = 8)
	public void testDeviceInformationSection() {
		executor.executeTest("Open device information section", true, () -> {
			page.ClickDeviceInformation();
			return true;
		});
	}

	@Test(priority = 9)
	public void testDeviceUin() {
		executor.executeTest("Read device UIN number", "inputValue", page::DeviceUINNumber);
	}

	@Test(priority = 10)
	public void testDeviceImei() {
		executor.executeTest("Read device IMEI number", "inputValue", page::DeviceIMEINumber);
	}

	@Test(priority = 11)
	public void testDeviceIccid() {
		executor.executeTest("Read device ICCID number", "inputValue", page::DeviceICCIDNumber);
	}

	@Test(priority = 12)
	public void testDeviceModel() {
		executor.executeTest("Read device model", "inputValue", page::DeviceModel);
	}

	@Test(priority = 13)
	public void testDeviceMake() {
		executor.executeTest("Read device make", "inputValue", page::DeviceMake);
	}

	@Test(priority = 14)
	public void testPrimaryOperatorInfo() {
		executor.executeTest("Read primary operator name", "inputValue", page::DevicePriOprName);
		executor.executeTest("Read primary operator number", "inputValue", page::DevicePriOprNumber);
	}

	@Test(priority = 15)
	public void testSecondaryOperatorInfo() {
		executor.executeTest("Read secondary operator name", "inputValue", page::DeviceSecOprName);
		executor.executeTest("Read secondary operator number", "inputValue", page::DeviceSecOprNumber);
	}

	@Test(priority = 16)
	public void testVehicleOwnerSection() {
		executor.executeTest("Open vehicle owner information section", true, () -> {
			page.ClickVehicleOwnerInformation();
			return true;
		});
	}

	@Test(priority = 17)
	public void testVehicleOwnerFields() {
		executor.executeTest("Read owner name", "inputValue", page::DeviceVehicleOwnerName);
		executor.executeTest("Read owner mobile", "inputValue", page::VehicleOwnerMobileNumber);
		executor.executeTest("Read owner POA doc name", "inputValue", page::VehicleOwnerPOADocName);
		executor.executeTest("Read owner POA doc number", "inputValue", page::VehicleOwnerPOADocNumber);
		executor.executeTest("Read owner POI doc name", "inputValue", page::VehicleOwnerPOIDocName);
		executor.executeTest("Read owner POI doc number", "inputValue", page::VehicleOwnerPOIDocNumber);
		executor.executeTest("Read owner address", "inputValue", page::VehicleOwnerAddress);
	}

	@Test(priority = 18)
	public void testVehicleInformationSection() {
		executor.executeTest("Open vehicle information section", true, () -> {
			page.ClickVehicleInformation();
			return true;
		});
	}

	@Test(priority = 19)
	public void testVehicleInformationFields() {
		executor.executeTest("Read vehicle model", "inputValue", page::VehicleModel);
		executor.executeTest("Read vehicle make", "inputValue", page::VehicleMake);
		executor.executeTest("Read vehicle manufacture year", "inputValue", page::VehicleManufactureYear);
		executor.executeTest("Read vehicle chassis number", "inputValue", page::VehicleChassisNumber);
		executor.executeTest("Read vehicle engine number", "inputValue", page::VehicleEngineNumber);
		executor.executeTest("Read vehicle registration number", "inputValue", page::VehicleRegistrationNumber);
		executor.executeTest("Read vehicle invoice date", "inputValue", page::VehicleInvoiceDate);
		executor.executeTest("Read vehicle invoice number", "inputValue", page::VehicleInvoiceNumber);
		executor.executeTest("Read vehicle RTO state", "inputValue", page::VehicleRTOState);
		executor.executeTest("Read vehicle RTO code", "inputValue", page::VehicleRTOCode);
	}

	@Test(priority = 20)
	public void testIgnButtonVisible() {
		executor.executeTest("Validate vehicle IGN button visibility", true, () -> {
			WebElement button = page.VehicleIGNButton();
			return button != null;
		});
	}

	@Test(priority = 21)
	public void testIgnStatusField() {
		executor.executeTest("Read vehicle IGN status", "inputValue", page::VehicleIGNSatus);
	}

	@Test(priority = 22)
	public void testDealerInformationSection() {
		executor.executeTest("Open dealer information section", true, () -> {
			page.ClickDealerInformation();
			return true;
		});
	}

	@Test(priority = 23)
	public void testDealerInformationFields() {
		executor.executeTest("Read dealer code", "inputValue", page::VehicleDealerCode);
		executor.executeTest("Read dealer email", "inputValue", page::VehicleDealerEmail);
		executor.executeTest("Read dealer city", "inputValue", page::VehicleDealerCity);
		executor.executeTest("Read dealer phone", "inputValue", page::VehicleDealerPhoneNo);
		executor.executeTest("Read POS name", "inputValue", page::VehicleDealerPOSName);
		executor.executeTest("Read POS code", "inputValue", page::VehicleDealerPOSCode);
	}

	@Test(priority = 24)
	public void testDeviceFotaSection() {
		executor.executeTest("Open Device FOTA status section", true, () -> {
			page.ClickDeviceFOTAStatus();
			return true;
		});
	}

	@Test(priority = 25)
	public void testDeviceFotaFields() {
		executor.executeTest("Read FOTA batch id", "inputValue", page::DeviceFOTABatchID);
		executor.executeTest("Read current firmware version", "inputValue", page::DeviceCurrentFWVer);
		executor.executeTest("Read assigned firmware version", "inputValue", page::DeviceAssignedFWVer);
		executor.executeTest("Read FOTA status", "inputValue", page::DeviceFOTAStatus);
		executor.executeTest("Read FOTA progress", "inputValue", page::DeviceFOTAProgress);
		executor.executeTest("Read OTA primary IP", "inputValue", page::DeviceOTAPriIP);
		executor.executeTest("Read OTA primary IP status", "inputValue", page::DeviceOTAPriStatus);
		executor.executeTest("Read OTA secondary IP", "inputValue", page::DeviceOTASecIP);
		executor.executeTest("Read OTA secondary IP status", "inputValue", page::DeviceOTASecStatus);
		executor.executeTest("Read device enable OTA status", "inputValue", page::DeviceStateEnOTAStatus);
	}

	@Test(priority = 26)
	public void testStage2RestrictionFlow() {
		executor.executeTest("Select stage-2 restriction reason", "inputValue", page::DeviceStage2Restriction);
		executor.executeTest("Enter stage-2 restriction remark", "inputValue", page::DeviceFOTASkipRemark);
		executor.executeTest("Read stage-2 skipped by", "inputValue", page::DeviceFOTASkipBy);
		executor.executeTest("Click remove stage-2 restriction button", "inputValue",
				page::RemoveStage2RestrictionButton);
	}

	@Test(priority = 27)
	public void testSubstageUpdates() {
		executor.executeTest("Update substage: ignition on/off", "inputValue", page::DeviceIgnOnOff);
		executor.executeTest("Update substage: GSM network available", "inputValue", page::GSMNWAvailable);
		executor.executeTest("Update substage: GPS fix available", "inputValue", page::GPSFixAvailable);
	}
}
