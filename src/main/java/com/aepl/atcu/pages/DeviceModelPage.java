package com.aepl.atcu.pages;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.aepl.atcu.locators.DeviceModelPageLocators.*;
import com.aepl.atcu.util.PageActionsUtil;

public class DeviceModelPage {
	// Global variables
	private final WebDriver driver;
	private final WebDriverWait wait;
	private final PageActionsUtil commonMethod;
	private final Logger logger = LogManager.getLogger(DeviceModelPage.class);

	// Constructor
	public DeviceModelPage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		this.commonMethod = new PageActionsUtil(driver, wait);
	}

	// Methods Goes here
	public void clickNavBar() {
		List<WebElement> navBarLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(dropDown));

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

	public String clickDeviceModel() {
		// Click on the element 'Device Model' and return the current URL
		try {
			WebElement DevicemodelLink = commonMethod.waitForVisibility(DeviceModel);
			commonMethod.clickElement(DevicemodelLink);
			return driver.getCurrentUrl();
		} catch (Exception e) {
			logger.error("Error while clicking on Device Model option.", e);
			throw new RuntimeException("Failed to click on Device Model option", e);
		}
	}

	public String clickAddDeviceModel() {
		// Click on the element 'Add Device Model' and return the current URL
		try {
			WebElement DevicemodelLink = commonMethod.waitForVisibility(AddDeviceModel);
			commonMethod.clickElement(DevicemodelLink);
			return driver.getCurrentUrl();
		} catch (Exception e) {
			logger.error("Error while clicking on Add Device Model button.", e);
			throw new RuntimeException("Failed to click on Add Device Model button", e);
		}
	}

	public boolean CheckPageHeader(List<String> expectedHeaders) {
		try {
			List<WebElement> actualHeaders = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(pageHeader));

			logger.info("Taking table heading after the search");
			List<String> actualHeaderTexts = actualHeaders.stream().map(WebElement::getText)
					.collect(Collectors.toList());

			return actualHeaderTexts.equals(expectedHeaders);
		} catch (Exception e) {
			logger.error("Error during search or header validation", e);
			throw new RuntimeException("Search or validation failed", e);
		}
	}

	public boolean AddNewDeviceModel() {
		logger.info("Trying to add the new model");

		try {
			WebElement ModelName = wait.until(ExpectedConditions.visibilityOfElementLocated(ModelNameInput));
			WebElement ModelCode = wait.until(ExpectedConditions.visibilityOfElementLocated(ModelCodeInput));
			WebElement HWVersion = wait.until(ExpectedConditions.visibilityOfElementLocated(HWVersionInput));
			WebElement SubmitBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(SubmitButton));

			String ModelNameInput = "dghdhghgh";
			String ModelCodeInput = "true";
			String HWVersionInput = "tyvyi";

			ModelName.click();
			ModelName.sendKeys(ModelNameInput);
			ModelName.sendKeys(Keys.TAB);

			ModelCode.sendKeys(ModelCodeInput);
			ModelCode.sendKeys(Keys.TAB);

			HWVersion.sendKeys(HWVersionInput);

			logger.info("Clicking on the save file button");
			SubmitBtn.click();
		} catch (Exception e) {
			logger.error("An error occurred while adding or validating the file", e);
			throw new RuntimeException("File addition or validation failed", e);
		}

		logger.error("File validation failed. File name or date-time not found in the table");
		return false;
	}

	public boolean checkSearchBoxAndTable() {
//		logger.log(Level.INFO, "Trying to check the search box and table");
		String modelName = "dghdhghgh";
		List<String> expectedHeaders = Arrays.asList("Model Name", "Model Code", "Hw Version", "Action");

//		logger.log(Level.INFO, "Taking table heading before the search");
		return commonMethod.checkSearchBoxWithTableHeadings(modelName, expectedHeaders);
	}

	public String clickViewIcon() {
		// Click on the element 'Device Model' and return the current URL
		try {
			WebElement ViewLink = wait.until(ExpectedConditions.visibilityOfElementLocated(EyeActionBtn));
			ViewLink.click();
			return driver.getCurrentUrl();
		} catch (Exception e) {
			logger.error("Error while clicking on Device Model option.", e);
			throw new RuntimeException("Failed to click on Device Model option", e);
		}
	}

	public boolean UpdateDeviceModel() {
		logger.info("Trying to update model");

		try {
			WebElement ModelName = wait.until(ExpectedConditions.visibilityOfElementLocated(ModelNameInput));
			WebElement ModelCode = wait.until(ExpectedConditions.visibilityOfElementLocated(ModelCodeInput));
			WebElement HWVersion = wait.until(ExpectedConditions.visibilityOfElementLocated(HWVersionInput));
			WebElement UpdateBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(UpdateButton));

			String ModelNameInput = "dghdhghgh123";
			String ModelCodeInput = "true1";
			String HWVersionInput = "tyvyi1";

			ModelName.click();
			ModelName.sendKeys(ModelNameInput);
			ModelName.sendKeys(Keys.TAB);

			ModelCode.sendKeys(ModelCodeInput);
			ModelCode.sendKeys(Keys.TAB);

			HWVersion.sendKeys(HWVersionInput);

			logger.info("Clicking on the save file button");
			UpdateBtn.click();
		} catch (Exception e) {
			logger.error("An error occurred while adding or validating the file", e);
			throw new RuntimeException("File addition or validation failed", e);
		}

		logger.error("File validation failed. File name or date-time not found in the table");
		return false;
	}

	public boolean checkSearchBoxAndTable2() {
//		logger.log(Level.INFO, "Trying to check the search box and table");
		String modelName = "dghdhghgh";
		List<String> expectedHeaders = Arrays.asList("Model Name", "Model Code", "Hw Version", "Action");

//		logger.log(Level.INFO, "Taking table heading before the search");
		return commonMethod.checkSearchBoxWithTableHeadings(modelName, expectedHeaders);
	}

	public boolean clickDeleteIcon() {
		// Click on the element 'Device Model' and return the current URL
		try {
			WebElement DltLink = wait.until(ExpectedConditions.visibilityOfElementLocated(DltActionBtn));
			DltLink.click();
			
			Thread.sleep(2000);
//			return driver.getCurrentUrl();
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			logger.info("Alert text: " + alertText);
			alert.accept();
			if (alertText.contains("Are you Sure?")) {
				alert.accept(); // Not accepting the alert to delete the item
				logger.info("Alert accepted.");
				driver.navigate().back();
				return true;
			} else {
				logger.warn("Unexpected alert text: " + alertText);
				alert.dismiss();
				return false;

			}
		} catch (Exception e) {
			logger.error("Error while clicking on Device Model option.", e);
			throw new RuntimeException("Failed to click on Device Model option", e);
		}
	}

	public void checkPagination() {
		try {
			WebElement nextButton = wait.until(ExpectedConditions.visibilityOfElementLocated(nextBtn));
			WebElement prevButton = wait.until(ExpectedConditions.visibilityOfElementLocated(prevBtn));
			WebElement actButton = wait.until(ExpectedConditions.visibilityOfElementLocated(activeBtn));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", nextButton);

			Thread.sleep(2000);

			commonMethod.checkPagination(nextButton, prevButton, actButton);

		} catch (Exception e) {
			logger.error("Error while validating Device Model pagination.", e);
		}
	}
}

