package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class DealerFotaPageLocators {

	protected final By navBarLink = By.xpath("//*[@id=\"navbarDropdownProfile\"]/span");
	protected final By dealerFota = By.xpath("//a[@class=\"dropdown-item ng-star-inserted\"][6]");
	protected final By addApprovedFileBtn = By.xpath("/html/body/app-root/app-dealer-fota/div/div/div[2]/button");
	protected final By fileNameInput = By.tagName("input");
	protected final By saveFileButton = By.xpath("//button[@class='btn btn-primary w-100']");
	protected final By tableRowsLocator = By.xpath("//tr[@class=\"odd text-center ng-star-inserted\"]");
	protected final By searchBox = By.name("searchInput");
	protected final By tableHeadings = By.xpath("//tr[@class=\"text-center\"]");
	protected final By deleteBtn = By.xpath("//i[@class=\"mat-tooltip-trigger fas fa-trash pl-3 ng-star-inserted\"]");
	protected final By nextBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	protected final By prevBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	protected final By activeBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
}
