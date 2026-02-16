package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class DispatchedDevicePageLocators {

	protected final By dropDown = By.xpath("//span[@class='headers_custom color_3D5772']");
	protected final By DispatchedDevice = By.xpath("//a[@routerlink='dispatch-device-page']");
	protected final By AddDispatchedDevice = By.xpath("//button[contains(.,'Add Dispatched Device')]");
	protected final By downloadSampleButton = By.xpath("//button[contains(.,'Download Sample')]");
	protected final By fileUploadInput = By.id("txtFileUpload");
}
