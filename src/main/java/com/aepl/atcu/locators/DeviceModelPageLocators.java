package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class DeviceModelPageLocators {
    public static final By ModelNameInput = By.xpath("//input[@formcontrolname='modelName']");
    public static final By ModelCodeInput = By.xpath("//input[@formcontrolname='modelCode']");
    public static final By HWVersionInput = By.xpath("//input[@formcontrolname='hwVersion']");
    public static final By SubmitButton = By.xpath("//button[contains(.,'Submit')]");
    public static final By UpdateButton = By.xpath("//button[contains(.,'Update')]");
    public static final By dropDown = By.xpath("//span[@class='headers_custom color_3D5772']");
    public static final By DeviceModel = By.xpath("//a[@routerlink='model']");
    public static final By AddDeviceModel = By.xpath("//button[contains(.,'Add Device Model')]");
    public static final By pageHeader = By.xpath("//h4[@class='h4ssssss text-cente mt-1']");
    public static final By EyeActionBtn = By.xpath("//mat-icon[@mattooltip='View']");
    public static final By DltActionBtn = By.xpath("//mat-icon[@mattooltip='Delete']");
    public static final By nextBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
    public static final By prevBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
    public static final By activeBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
}

