package com.aepl.atcu.locators;

import org.openqa.selenium.By;

public class DeviceDashboardPageLocators {
	public static final By DASHBOARD = By.xpath("//a[contains(@class, 'dropdown-toggle') and contains(text(), 'Dashboard')]");
	public static final By DEVICE_DASHBOARD = By.xpath("//a[@routerlink='device-dashboard']");
	public static final By KPI_CARDS_1 = By.xpath("//div[@class = 'kpi-card kpi-gradient-blue']"); // gives list of cards
	public static final By DOWNLOAD_REPORT_BTN = By.cssSelector("button[class='primary-button ng-star-inserted']");
}
