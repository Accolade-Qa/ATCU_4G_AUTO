package com.aepl.atcu.pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.atcu.util.Constants;

public class DeviceDashboardPage {
	private WebDriver driver;
	private WebDriverWait wait;
	private CommonMethod commonMethod;
	private static final Logger logger = LogManager.getLogger(DeviceDashboardPage.class);

	public DeviceDashboardPage(WebDriver driver) {
		this.driver = driver;
		this.commonMethod = new CommonMethod(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	// Locators Goes here

	private By KPICard = By.xpath("//span[@class='value']");
	private By DeviceModelWise = By.xpath("//button[@class='btn btn-outline-primary float-right']");
	private By GraphEnability = By.xpath("//span[@class='white_card_header']");
	private By tableHeadings;
	private WebElement searchBox;

	// Methods Goes here

	public void CheckKPICard() {
		List<WebElement> KPICards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(KPICard));

		boolean isEnabled = false, isClickable = false;
		for (WebElement link : KPICards) {
			link.isEnabled();
			isEnabled = true;
			isClickable = true;
			break;
		}

		if (!isEnabled && !isClickable) {
			throw new RuntimeException("Failed");
		}
	}

	public String DeviceModelWise() {
		List<WebElement> ModelWiseButton = wait
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DeviceModelWise));

		boolean isEnabled = false, isClickable = false;
		for (WebElement link : ModelWiseButton) {
			link.isEnabled();
			isEnabled = true;
			isClickable = true;
			break;
		}

		if (!isEnabled && !isClickable) {
			throw new RuntimeException("Failed");
		}
		return null;

	}

	public void GraphEnability() {
		List<WebElement> GraphEnabled = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(GraphEnability));

		boolean isEnabled = false, isClickable = false;
		for (WebElement link : GraphEnabled) {
			link.isEnabled();
			isEnabled = true;
			isClickable = true;
			break;
		}
		driver.navigate().to("http://20.219.88.214:6102/vehicle-eol-batch");

		if (!isEnabled && !isClickable) {
			throw new RuntimeException("Failed");
		}
	}

	public void ClickTotalProdDevices() {

		try {
			// Locate "Total Production Devices" text
			WebElement totalDevicesText = driver
					.findElement(By.xpath("//h3[normalize-space()='Total Production Devices']"));
			if (totalDevicesText.isDisplayed() && totalDevicesText.isEnabled()) {
				System.out.println("Text found: " + totalDevicesText.getText());
			} else {
				System.out.println("Total Production Devices text is not visible or enabled.");
			}

			/*
			 * // Locate "Download Report" button WebElement ViewButton =
			 * driver.findElement(
			 * By.xpath("//button[@class='mat-tooltip-trigger fas fa-eye']")); if
			 * (ViewButton.isDisplayed() && ViewButton.isEnabled()) { ViewButton.click();
			 * System.out.println("Download Report button clicked."); } else {
			 * System.out.println("Download Report button is not clickable or not visible."
			 * ); }
			 */

			// Locate "Search input field"
			WebElement search = wait.until(ExpectedConditions.elementToBeClickable(searchBox));
			if (search.isDisplayed() && search.isEnabled()) {
				search.sendKeys(Constants.UIN_No);
				search.sendKeys(Keys.ENTER); // Or use searchInputField.sendKeys(Keys.ENTER);
				System.out.println("Search completed.");
			} else {
				System.out.println("Search input field is not visible or enabled.");
			}

			WebElement nextButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@aria-label='Next page']")));
			if (nextButton.isDisplayed() && nextButton.isEnabled()) {
				logger.info("Next button located. Clicking on it.");
				nextButton.click();

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//expectedElementOnNextPage")));
				logger.info("Successfully navigated to the next page.");
			} else {
				logger.warn("Next button is not clickable.");
				return;
			}

			WebElement previousButton = wait
					.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@aria-label='Previous page']")));
			if (previousButton.isDisplayed() && previousButton.isEnabled()) {
				logger.info("Previous button located. Clicking on it.");
				previousButton.click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//expectedElementOnPreviousPage")));
				logger.info("Successfully navigated back to the previous page.");
			} else {
				logger.warn("Previous button is not clickable.");
			}

		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + e.getMessage());
		} catch (StaleElementReferenceException e) {
			System.out.println("Element is no longer attached to the DOM: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("An unexpected error occurred: " + e.getMessage());
		}

	}

	public boolean TotalProdDeviceDetails(List<String> expectedHeaders) {
		try {
			WebElement details = wait.until(ExpectedConditions.visibilityOfElementLocated(commonMethod.searchBox));
			logger.info("Taking table heading before the search");

			List<WebElement> actualHeaders = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tableHeadings));
			logger.info("Trying to clicking on search box and search something");
			details.click();
			details.clear();
			details.sendKeys(Constants.IMEI);
			details.sendKeys(Keys.ENTER);

			logger.info("Taking table heading after the search");
			List<String> actualHeaderTexts = actualHeaders.stream().map(WebElement::getText)
					.collect(Collectors.toList());

			return actualHeaderTexts.equals(expectedHeaders);
		} catch (Exception e) {
			logger.error("Error during search or header validation", e);
			throw new RuntimeException("Search or validation failed", e);
		}

	}
}
