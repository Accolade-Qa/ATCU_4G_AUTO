package com.aepl.atcu.tests;

import java.awt.AWTException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aepl.atcu.base.TestBase;
import com.aepl.atcu.pages.DispachedDevicePage;
import com.aepl.atcu.util.ConfigProperties;
import com.aepl.atcu.util.ExcelUtility;

public class DispatchedDevicePageTest extends TestBase {

	private DispachedDevicePage dispatchedDevice;
	private ExcelUtility excel;
	private SoftAssert softAssert;
	private Executor executor;

	@Override
	@BeforeClass
	public void setUp() {
		super.setUp();
		this.dispatchedDevice = new DispachedDevicePage(driver);
		this.excel = new ExcelUtility();
		this.softAssert = new SoftAssert();
		this.executor = new Executor(excel, softAssert);
		this.excel.initializeExcel("Dispatched Device");
		logger.info("Setup completed for DispatchedDevicePageTest");
	}

	@Test(priority = 1)
	public void testSessionIsActive() {
		executor.executeTest("Validate active logged-in session", true,
				() -> !driver.getCurrentUrl().toLowerCase().contains("/login"));
	}

	@Test(priority = 2)
	public void testClickNavBar() {
		executor.executeTest("Click Device Utility from navigation", true, () -> {
			dispatchedDevice.clickNavBar();
			String expectedUrl = ConfigProperties.getProperty("dashboard.url");
			String currentUrl = driver.getCurrentUrl();
			return currentUrl != null && expectedUrl != null
					&& currentUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 3)
	public void testClickOnDispatchedDevice() {
		executor.executeTest("Open Dispatched Device page", true, () -> {
			String expectedUrl = ConfigProperties.getProperty("device.model");
			String actualUrl = dispatchedDevice.clickDispatchedDevice();
			return actualUrl != null && expectedUrl != null && actualUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 4)
	public void testClickAddDispatchedDevice() {
		executor.executeTest("Open Add Dispatched Device page", true, () -> {
			String expectedUrl = ConfigProperties.getProperty("Add.Dispatch.Devices");
			String actualUrl = dispatchedDevice.clickAddDispatchedDevice();
			return actualUrl != null && expectedUrl != null && actualUrl.toLowerCase().contains(expectedUrl.toLowerCase());
		});
	}

	@Test(priority = 5)
	public void testDownloadSample() {
		executor.executeTest("Validate Download Sample action", true, () -> {
			dispatchedDevice.clickdownloadSample();
			return true;
		});
	}

	@Test(priority = 6)
	public void testFileUpload() {
		executor.executeTest("Validate dispatch sample upload", true, () -> {
			String directory = "C:\\Users\\Dhananjay Jagtap\\Downloads";
			String filePrefix = "Sample_Dispatch_Sheet";
			try {
				dispatchedDevice.uploadFile(directory, filePrefix);
				return true;
			} catch (AWTException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
