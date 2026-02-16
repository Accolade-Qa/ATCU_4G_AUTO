package com.aepl.atcu.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.aepl.atcu.locators.CommonPageLocators.*;

public class PageActionsUtil {

	private static final Logger logger = LogManager.getLogger(PageActionsUtil.class);

	private final WebDriver driver;
	private final WebDriverWait wait;
	private final TableUtils tableUtils;

	public PageActionsUtil(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = (wait != null) ? wait : new WebDriverWait(driver, Duration.ofSeconds(10));
		this.tableUtils = new TableUtils(driver, this.wait);
	}

	public void captureScreenshot(String testCaseName) {
		if (!(driver instanceof TakesScreenshot)) {
			throw new IllegalStateException("Driver does not implement TakesScreenshot.");
		}
		try {
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String fileName = testCaseName + "_" + timestamp + ".png";
			String screenshotDir = System.getProperty("user.dir") + File.separator + "screenshots";
			File dir = new File(screenshotDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(srcFile, new File(screenshotDir + File.separator + fileName));
		} catch (IOException e) {
			logger.error("Failed to capture screenshot for {}", testCaseName, e);
		}
	}

	public void highlightElement(WebElement element, String colorCode) {
		((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px " + colorCode + " '", element);
	}

	public WebElement findFirstVisibleElement(By... locators) {
		for (By locator : locators) {
			List<WebElement> elements = driver.findElements(locator);
			for (WebElement element : elements) {
				if (element != null && element.isDisplayed()) {
					return element;
				}
			}
		}
		return null;
	}

	public boolean isElementVisible(By... locators) {
		return findFirstVisibleElement(locators) != null;
	}

	public boolean isElementEnabled(By... locators) {
		WebElement element = findFirstVisibleElement(locators);
		return element != null && element.isEnabled();
	}

	public WebElement waitForVisibility(By locator) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public void clickElement(WebElement element) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		}
	}

	public String getElementText(By... locators) {
		WebElement element = findFirstVisibleElement(locators);
		return element != null ? element.getText().trim() : "";
	}

	public void typeAndSearch(By inputLocator, By searchButtonLocator, String query) {
		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(inputLocator));
		searchInput.click();
		searchInput.clear();
		searchInput.sendKeys(query);

		WebElement searchButton = findFirstVisibleElement(searchButtonLocator);
		if (searchButton != null && searchButton.isEnabled()) {
			clickElement(searchButton);
		} else {
			searchInput.sendKeys(Keys.ENTER);
		}
	}

	public void resetSearch(By inputLocator, By resetButtonLocator) {
		WebElement searchInput = findFirstVisibleElement(inputLocator);
		WebElement resetButton = findFirstVisibleElement(resetButtonLocator);

		if (resetButton != null && resetButton.isEnabled()) {
			clickElement(resetButton);
		} else if (searchInput != null) {
			searchInput.click();
			searchInput.clear();
			searchInput.sendKeys(Keys.ENTER);
		}
	}

	public boolean checkSearchBoxWithTableHeadings(String input, List<String> expectedHeaders) {
		checkSearchBox(input);
		List<String> actualHeaders = tableUtils.getTableHeaders(TABLE).stream().map(String::trim).map(String::toLowerCase)
				.collect(Collectors.toList());
		List<String> expected = expectedHeaders.stream().map(String::trim).map(String::toLowerCase)
				.collect(Collectors.toList());
		return actualHeaders.equals(expected);
	}

	public void checkSearchBox(String input) {
		WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_BOX_INPUT));
		search.click();
		search.clear();
		search.sendKeys(input);
		search.sendKeys(Keys.ENTER);
	}

	public boolean checkTableHeadings(List<String> expectedHeaders) {
		List<String> actualHeaders = tableUtils.getTableHeaders(TABLE).stream().map(String::trim).map(String::toLowerCase)
				.collect(Collectors.toList());
		List<String> expected = expectedHeaders.stream().map(String::trim).map(String::toLowerCase)
				.collect(Collectors.toList());
		return actualHeaders.equals(expected);
	}

	public void clickEyeActionButton(By eyeActionButton) {
		WebElement eyeBtn = wait.until(ExpectedConditions.elementToBeClickable(eyeActionButton));
		clickElement(eyeBtn);
	}

	public void checkPagination(WebElement nextButton, WebElement prevButton, WebElement activeButton) {
		if (nextButton.isDisplayed() && nextButton.isEnabled()) {
			clickElement(nextButton);
		}
		if (prevButton.isDisplayed() && prevButton.isEnabled()) {
			clickElement(prevButton);
		}
	}

	public void checkPagination() {
		WebElement rightArrow = findFirstVisibleElement(RIGHT_ARROW);
		WebElement leftArrow = findFirstVisibleElement(LEFT_ARROW);
		if (rightArrow != null && rightArrow.isEnabled()) {
			clickElement(rightArrow);
		}
		if (leftArrow != null && leftArrow.isEnabled()) {
			clickElement(leftArrow);
		}
	}

	public void reportDownloadButtons(WebElement button) {
		clickElement(button);
		try {
			Alert alert = wait.until(ExpectedConditions.alertIsPresent());
			alert.accept();
		} catch (Exception ignored) {
			// No alert is a valid state for direct-download flows.
		}
	}

	public void checkReportDownloadForAllbuttons(WebElement button) {
		if (button == null || !button.isDisplayed() || !button.isEnabled()) {
			throw new IllegalStateException("Download button is not visible/enabled.");
		}
	}

	public String randomStringGen() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase(Locale.ROOT);
	}
}

