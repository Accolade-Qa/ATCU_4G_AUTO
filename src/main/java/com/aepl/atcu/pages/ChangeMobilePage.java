package com.aepl.atcu.pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.atcu.util.Constants;

public class ChangeMobilePage {
	private final WebDriver driver;
	private final WebDriverWait wait;
	private static final Logger logger = LogManager.getLogger(ChangeMobilePage.class);

	public ChangeMobilePage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	// Locators Goes here
	private final By navBarLink = By.xpath("//span[@class=\"headers_custom color_3D5772\"]");
	private final By changeMobile = By.xpath("//a[@class=\"dropdown-item ng-star-inserted\"][8]");
	private final By searchBox = By.xpath("//input[@placeholder=\"Search and Press Enter\"]");
	private final By tableHeadings = By.xpath("//tr[@class=\"text-center\"]");
	private final By eyeActionButtons = By.xpath("//td[@class = \"ng-star-inserted\"][1]");
	private final By deleteActionButtons = By.xpath("//td[@class = \"ng-star-inserted\"][2]");
	private final By paginationNextButton = By.xpath("//li[@class=\\\"pagination-next ng-star-inserted\\\"");
	private final By paginationPreviousButton = By.xpath("//li[@class=\\\"pagination-previous ng-star-inserted\\\"]");

	// Methods Goes here
	public void clickNavBar() {
		List<WebElement> navBarLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(navBarLink));

		boolean isClicked = false;
		for (WebElement link : navBarLinks) {
			if (link.getText().equalsIgnoreCase("Device Utility")) {
				link.click();
				isClicked = true;
				break;
			}
		}
		if (!isClicked) {
			throw new RuntimeException("Failed to find and click on 'Device Utility' in the navigation bar.");
		}
	}

	public String clickDropDownOption() {
		// Click on the element 'changeMobile' and return the current URL
		try {
			WebElement changeMobileLink = wait.until(ExpectedConditions.visibilityOfElementLocated(changeMobile));
			changeMobileLink.click();
			return driver.getCurrentUrl();
		} catch (Exception e) {
			logger.error("Error while clicking on Change Mobile option.", e);
			throw new RuntimeException("Failed to click on Change Mobile option", e);
		}
	}

	public boolean checkSearchBox(String iccid, List<String> expectedHeaders) {
		try {
			WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));

			logger.info("Taking table heading before the search");
			List<WebElement> actualHeaders = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tableHeadings));

			logger.info("Trying to clicking on search box and search something");
			search.click();
			search.clear();
			search.sendKeys(iccid);
			search.sendKeys(Keys.ENTER);

			logger.info("Taking table heading after the search");
			List<String> actualHeaderTexts = actualHeaders.stream().map(WebElement::getText)
					.collect(Collectors.toList());

			return actualHeaderTexts.equals(expectedHeaders);
		} catch (Exception e) {
			logger.error("Error during search or header validation", e);
			throw new RuntimeException("Search or validation failed", e);
		}
	}

	public void clickEyeActionButton() {
		logger.info("Locating the eye action button...");
		try {
			WebElement eyeButton = wait.until(ExpectedConditions.visibilityOfElementLocated(eyeActionButtons));
			logger.info("Eye action button located. Clicking on it.");
			eyeButton.click();

			logger.info("Performing actions on the navigated page...");
			// Adding a more structured way to wait and validate navigation
			wait.until(ExpectedConditions.urlContains(Constants.CHANGE_MOBILE_VIEW));

			logger.info("Page validation successful. Navigating back.");
			driver.navigate().back();
			wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
			logger.info("Navigated back to the original page.");
		} catch (Exception e) {
			logger.error("An error occurred while interacting with the eye action button.", e);
			throw new RuntimeException("Failed to process the eye action button.", e);
		}
	}

	public boolean clickDeleteActionButton() {
		logger.info("Attempting to click the Delete Button...");
		try {
			WebElement deleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(deleteActionButtons));
			if (deleteButton.isEnabled() && deleteButton.isDisplayed()) {
				deleteButton.click();
				logger.info("Delete Button clicked. Validating alert...");

				Alert alert = driver.switchTo().alert();
				String alertText = alert.getText();
				logger.info("Alert text: " + alertText);

				if (alertText.contains("Are you sure you want to delete?")) {
//	                alert.accept(); // Not accepting the alert to delete the item
					logger.info("Alert accepted.");
					driver.navigate().back();
					return true;
				} else {
					logger.warn("Unexpected alert text: " + alertText);
					alert.dismiss();
					return false;
				}
			}
		} catch (NoSuchElementException e) {
			logger.error("Delete Button not found: " + e.getMessage());
		} catch (Exception e) {
			logger.error("An error occurred while clicking Delete Button: " + e.getMessage());
		}
		return false;
	}

	public void checkPagination() {
		logger.info("Checking pagination functionality...");

		try {
			WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(paginationNextButton));
			if (nextButton.isDisplayed() && nextButton.isEnabled()) {
				logger.info("Next button located. Clicking on it.");
				nextButton.click();

				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//expectedElementOnNextPage")));
				logger.info("Successfully navigated to the next page.");
			} else {
				logger.warn("Next button is not clickable.");
				return;
			}

			WebElement previousButton = wait.until(ExpectedConditions.elementToBeClickable(paginationPreviousButton));
			if (previousButton.isDisplayed() && previousButton.isEnabled()) {
				logger.info("Previous button located. Clicking on it.");
				previousButton.click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//expectedElementOnPreviousPage")));
				logger.info("Successfully navigated back to the previous page.");
			} else {
				logger.warn("Previous button is not clickable.");
			}
		} catch (Exception e) {
			logger.error("An error occurred while testing pagination.", e);
			throw new RuntimeException("Pagination test failed.", e);
		}
	}
}
