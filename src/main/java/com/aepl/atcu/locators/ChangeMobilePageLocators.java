package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class ChangeMobilePageLocators {
    public static final By navBarLink = By.xpath("//span[@class=\"headers_custom color_3D5772\"]");
    public static final By changeMobile = By.xpath("//a[@class=\"dropdown-item ng-star-inserted\"][8]");
    public static final By searchBox = By.xpath("//input[@placeholder=\"Search and Press Enter\"]");
    public static final By tableHeadings = By.xpath("//tr[@class=\"text-center\"]");
    public static final By eyeActionButtons = By.xpath("//td[@class = \"ng-star-inserted\"][1]");
    public static final By deleteActionButtons = By.xpath("//td[@class = \"ng-star-inserted\"][2]");
    public static final By paginationNextButton = By.xpath("//li[@class=\\\"pagination-next ng-star-inserted\\\"");
    public static final By paginationPreviousButton = By.xpath("//li[@class=\\\"pagination-previous ng-star-inserted\\\"]");
    public static final By expectedElementOnNextPage = By.xpath("//expectedElementOnNextPage");
    public static final By expectedElementOnPreviousPage = By.xpath("//expectedElementOnPreviousPage");
}