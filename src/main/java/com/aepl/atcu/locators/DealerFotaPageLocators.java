package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class DealerFotaPageLocators {
    public static final By navBarLink = By.xpath("//*[@id=\"navbarDropdownProfile\"]/span");
    public static final By dealerFota = By.xpath("//a[@class=\"dropdown-item ng-star-inserted\"][6]");
    public static final By addApprovedFileBtn = By.xpath("/html/body/app-root/app-dealer-fota/div/div/div[2]/button");
    public static final By fileNameInput = By.tagName("input");
    public static final By saveFileButton = By.xpath("//button[@class='btn btn-primary w-100']");
    public static final By tableRowsLocator = By.xpath("//tr[@class=\"odd text-center ng-star-inserted\"]");
    public static final By searchBox = By.name("searchInput");
    public static final By tableHeadings = By.xpath("//tr[@class=\"text-center\"]");
    public static final By deleteBtn = By.xpath("//i[@class=\"mat-tooltip-trigger fas fa-trash pl-3 ng-star-inserted\"]");
    public static final By nextBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
    public static final By prevBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
    public static final By activeBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
}