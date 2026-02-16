package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class OtaPageLocators {
    public static final By navBarLink = By.xpath("//span[@class='headers_custom color_3D5772']");
    public static final By otaLink = By.xpath("//a[@class='dropdown-item ng-star-inserted'][4]");
    public static final By buttonsList = By.xpath("//button[@class='btn btn-outline-primary ng-star-inserted']");
    public static final By nextBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
    public static final By prevBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
    public static final By activeBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
    public static final By eyeActionButton = By.xpath("//mat-icon[@mattooltip='View']");
    public static final By calendar = By.xpath("//button[@class=\"mat-focus-indicator mat-icon-button mat-button-base\"]");
    public static final By batchIdFrom = By.id("fromBatchId");
    public static final By batchIdTo = By.id("toBatchId");
    public static final By batchSubmitBtn = By.xpath("//button[@class=\"btn-sm btn btn-outline-primary example-full-width\"]");
    public static final By clearButton = By.xpath("//button[@class=\"btn-sm btn btn-outline-secondary example-full-width\"]");
    public static final By reportButton = By.xpath("//button[@class=\"btn-sm btn example-full-width float-right\"]");
    public static final By allInputFields = By.tagName("input");
    public static final By toastMessageOfOtaAdd = By.xpath("//simple-snack-bar//span[text()='Success']");
    public static final By editButtonOfOta = By.xpath("//mat-icon[@class=\"mat-icon notranslate mx-2 material-icons mat-icon-no-color\"]");
    public static final By deleteButtonOfOta = By.xpath("//mat-icon[class=\"mat-icon notranslate mat-tooltip-trigger delete-icon material-icons mat-icon-no-color\"]");
    public static final By dropdownOtaType = By.id("id=\"mat-select-6\"");
    public static final By otaBatchFirstRowId = By.xpath("//table/tbody/tr[1]/td[1]");
    public static final By otaActionColumnCell = By.xpath("//table/tbody/tr/td[9]");
    public static final By otaAddButton = By.xpath("//button[@class='btn btn-outline-primary ng-star-inserted']");
    public static final By otaUpdateButton = By.xpath("//button[@class=\"btn btn-outline-primary ml-1 ng-star-inserted\"]");
}

