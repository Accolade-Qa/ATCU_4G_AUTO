package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class DeviceDashboardPageLocators {
	public static final By DASHBOARD = By.xpath("//a[contains(@class, 'dropdown-toggle') and contains(text(), 'Dashboard')]");
	public static final By DEVICE_DASHBOARD = By.xpath("//a[@routerlink='device-dashboard']");
	public static final By KPI_CARDS_1 = By.xpath("//div[@class = 'kpi-card kpi-gradient-blue']"); // gives list of cards
	public static final By DOWNLOAD_REPORT_BTN = By.cssSelector("button[class='primary-button ng-star-inserted']");
	public static final By TABLE_ROOT = By.xpath("//table");
	public static final By SEARCH_INPUT = By.xpath("//input[contains(@placeholder,'Search') or contains(@placeholder,'search')]");
	public static final By SEARCH_BUTTON = By.xpath("//button[contains(@class,'search-btn') or .//mat-icon[contains(text(),'search')]]");
	public static final By RESET_BUTTON = By.xpath("//button[contains(@class,'reload-button') or .//mat-icon[contains(text(),'refresh')]]");
	public static final By PAGINATION_CONTAINER = By.xpath("//div[contains(@class,'component-footer') or contains(@class,'currentPage-numbers')]");
	public static final By PAGE_INFO = By.xpath("//span[contains(@class,'page-info')]");
	public static final By NEXT_PAGE_BUTTON = By.xpath("//button[.//mat-icon[contains(text(),'chevron_right')]]");
	public static final By PREVIOUS_PAGE_BUTTON = By.xpath("//button[.//mat-icon[contains(text(),'chevron_left')]]");
	public static final By PAGE_NUMBER_BUTTONS = By.xpath("//div[contains(@class,'currentPage-numbers')]//button[not(@disabled)]");
	public static final By KPI_CARDS = By.xpath("//div[contains(@class,'kpi-section')]/div");
	public static final By COMPONENT_TITLE = By.cssSelector(".component-title");
}
