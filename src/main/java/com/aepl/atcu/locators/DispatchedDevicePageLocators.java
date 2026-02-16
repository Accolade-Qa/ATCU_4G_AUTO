package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class DispatchedDevicePageLocators {
    public static final By dropDown = By.xpath("//span[@class='headers_custom color_3D5772']");
    public static final By DispatchedDevice = By.xpath("//a[@routerlink='dispatch-device-page']");
    public static final By AddDispatchedDevice = By.xpath("//button[contains(.,'Add Dispatched Device')]");
    public static final By downloadSampleButton = By.xpath("//button[contains(.,'Download Sample')]");
    public static final By fileUploadInput = By.id("txtFileUpload");
}

