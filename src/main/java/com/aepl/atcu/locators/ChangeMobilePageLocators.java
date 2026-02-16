package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class ChangeMobilePageLocators {

	protected final By navBarLink = By.xpath("//span[@class=\"headers_custom color_3D5772\"]");
	protected final By changeMobile = By.xpath("//a[@class=\"dropdown-item ng-star-inserted\"][8]");
	protected final By searchBox = By.xpath("//input[@placeholder=\"Search and Press Enter\"]");
	protected final By tableHeadings = By.xpath("//tr[@class=\"text-center\"]");
	protected final By eyeActionButtons = By.xpath("//td[@class = \"ng-star-inserted\"][1]");
	protected final By deleteActionButtons = By.xpath("//td[@class = \"ng-star-inserted\"][2]");
	protected final By paginationNextButton = By.xpath("//li[@class=\\\"pagination-next ng-star-inserted\\\"");
	protected final By paginationPreviousButton = By.xpath("//li[@class=\\\"pagination-previous ng-star-inserted\\\"]");
	protected final By expectedElementOnNextPage = By.xpath("//expectedElementOnNextPage");
	protected final By expectedElementOnPreviousPage = By.xpath("//expectedElementOnPreviousPage");
}
