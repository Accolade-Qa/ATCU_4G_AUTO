package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class DeviceModelPageLocators {

	protected final By ModelNameInput = By.xpath("//input[@formcontrolname='modelName']");
	protected final By ModelCodeInput = By.xpath("//input[@formcontrolname='modelCode']");
	protected final By HWVersionInput = By.xpath("//input[@formcontrolname='hwVersion']");
	protected final By SubmitButton = By.xpath("//button[contains(.,'Submit')]");
	protected final By UpdateButton = By.xpath("//button[contains(.,'Update')]");
	protected final By dropDown = By.xpath("//span[@class='headers_custom color_3D5772']");
	protected final By DeviceModel = By.xpath("//a[@routerlink='model']");
	protected final By AddDeviceModel = By.xpath("//button[contains(.,'Add Device Model')]");
	protected final By pageHeader = By.xpath("//h4[@class='h4ssssss text-cente mt-1']");
	protected final By EyeActionBtn = By.xpath("//mat-icon[@mattooltip='View']");
	protected final By DltActionBtn = By.xpath("//mat-icon[@mattooltip='Delete']");
	protected final By nextBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	protected final By prevBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	protected final By activeBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
}
