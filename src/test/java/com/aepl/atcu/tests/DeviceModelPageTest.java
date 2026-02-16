package com.aepl.atcu.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.atcu.base.TestBase;
import com.aepl.atcu.pages.DeviceModelPage;
import com.aepl.atcu.util.ConfigProperties;
import com.aepl.atcu.util.ExcelUtility;

public class DeviceModelPageTest extends TestBase {

	private DeviceModelPage deviceModel;
	private ExcelUtility excel;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.deviceModel = new DeviceModelPage(driver);
		this.excel = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excel, softAssert);
		this.excel.initializeExcel("Device Model");
		logger.info("Setup completed for DeviceModelPageTest");
	}

	@Test(priority = 1)
	public void testSessionIsActive() {
		executor.executeTest("Validate active logged-in session", true,
				() -> !driver.getCurrentUrl().toLowerCase().contains("/login"));
	}

	@Test(priority = 2)
	public void testClickNavBar() {
		executor.executeTest("Click Device Utility from navigation", true, () -> {
			deviceModel.clickNavBar();
			String expectedUrl = ConfigProperties.getProperty("dashboard.url");
			String currentUrl = driver.getCurrentUrl();
			return currentUrl != null && expectedUrl != null
					&& currentUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 3)
	public void testClickOnDeviceModel() {
		executor.executeTest("Open Device Model page", true, () -> {
			String expectedUrl = ConfigProperties.getProperty("device.model");
			String actualUrl = deviceModel.clickDeviceModel();
			return actualUrl != null && expectedUrl != null && actualUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 4)
	public void testClickAddDeviceModel() {
		executor.executeTest("Open Add Device Model page", true, () -> {
			String expectedUrl = ConfigProperties.getProperty("add.device.model");
			String actualUrl = deviceModel.clickAddDeviceModel();
			return actualUrl != null && expectedUrl != null && actualUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 5)
	public void testAddNewDeviceModel() {
		executor.executeTest("Validate add new device model", true, () -> {
			deviceModel.AddNewDeviceModel();
			String expectedUrl = ConfigProperties.getProperty("device.model");
			String actualUrl = driver.getCurrentUrl();
			return actualUrl != null && expectedUrl != null && actualUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 6)
	public void testCheckSearchBoxAndTable() {
		executor.executeTest("Validate Device Model search and table headers", true, deviceModel::checkSearchBoxAndTable);
	}

	@Test(priority = 7)
	public void testClickViewIcon() {
		executor.executeTest("Validate view icon navigation", true, () -> {
			String expectedUrl = ConfigProperties.getProperty("add.device.model");
			String actualUrl = deviceModel.clickViewIcon();
			return actualUrl != null && expectedUrl != null && actualUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 8)
	public void testUpdateDeviceModel() {
		executor.executeTest("Validate update device model", true, () -> {
			deviceModel.UpdateDeviceModel();
			String expectedUrl = ConfigProperties.getProperty("device.model");
			String actualUrl = driver.getCurrentUrl();
			return actualUrl != null && expectedUrl != null && actualUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 9)
	public void testCheckSearchBoxAndTable2() {
		executor.executeTest("Validate Device Model search and table headers after update", true,
				deviceModel::checkSearchBoxAndTable2);
	}

	@Test(priority = 10)
	public void testClickDeleteIcon() {
		executor.executeTest("Validate delete icon and alert flow", true, deviceModel::clickDeleteIcon);
	}

	@Test(priority = 11)
	public void testCheckPagination() {
		executor.executeTest("Validate Device Model pagination", true, () -> {
			deviceModel.checkPagination();
			return true;
		});
	}
}
