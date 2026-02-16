package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class OtaPageLocators {

	protected final By navBarLink = By.xpath("//span[@class='headers_custom color_3D5772']");
	protected final By otaLink = By.xpath("//a[@class='dropdown-item ng-star-inserted'][4]");
	protected final By buttonsList = By.xpath("//button[@class='btn btn-outline-primary ng-star-inserted']");
	protected final By nextBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	protected final By prevBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	protected final By activeBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	protected final By eyeActionButton = By.xpath("//mat-icon[@mattooltip='View']");
	protected final By calendar = By.xpath("//button[@class=\"mat-focus-indicator mat-icon-button mat-button-base\"]");
	protected final By batchIdFrom = By.id("fromBatchId");
	protected final By batchIdTo = By.id("toBatchId");
	protected final By batchSubmitBtn = By.xpath("//button[@class=\"btn-sm btn btn-outline-primary example-full-width\"]");
	protected final By clearButton = By.xpath("//button[@class=\"btn-sm btn btn-outline-secondary example-full-width\"]");
	protected final By reportButton = By.xpath("//button[@class=\"btn-sm btn example-full-width float-right\"]");
	protected final By allInputFields = By.tagName("input");
	protected final By toastMessageOfOtaAdd = By.xpath("//simple-snack-bar//span[text()='Success']");
	protected final By editButtonOfOta = By.xpath("//mat-icon[@class=\"mat-icon notranslate mx-2 material-icons mat-icon-no-color\"]");
	protected final By deleteButtonOfOta = By.xpath(
			"//mat-icon[class=\"mat-icon notranslate mat-tooltip-trigger delete-icon material-icons mat-icon-no-color\"]");
	protected final By dropdownOtaType = By.id("id=\"mat-select-6\"");
	protected final By otaBatchFirstRowId = By.xpath("//table/tbody/tr[1]/td[1]");
	protected final By otaActionColumnCell = By.xpath("//table/tbody/tr/td[9]");
	protected final By otaAddButton = By.xpath("//button[@class='btn btn-outline-primary ng-star-inserted']");
	protected final By otaUpdateButton = By.xpath("//button[@class=\"btn btn-outline-primary ml-1 ng-star-inserted\"]");
}
