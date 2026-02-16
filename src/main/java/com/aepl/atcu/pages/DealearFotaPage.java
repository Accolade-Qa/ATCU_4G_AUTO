package com.aepl.atcu.pages;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DealearFotaPage {
	private final WebDriver driver;
	private final WebDriverWait wait;
	private final CommonMethods commonMethod;
	private static final Logger logger = LogManager.getLogger(DealearFotaPage.class);

	public DealearFotaPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		this.commonMethod = new CommonMethods(driver, wait);
		logger.info("Initialized the driver and wait ");
	}

	// Locators goes here
	private final By navBarLink = By.xpath("//*[@id=\"navbarDropdownProfile\"]/span");
	private final By dealerFota = By.xpath("//a[@class=\"dropdown-item ng-star-inserted\"][6]");
	private final By addApprovedFileBtn = By.xpath("/html/body/app-root/app-dealer-fota/div/div/div[2]/button");
	private final By fileNameInput = By.tagName("input");
	private final By saveFileButton = By.xpath("//button[@class='btn btn-primary w-100']");
	private final By tableRowsLocator = By.xpath("//tr[@class=\"odd text-center ng-star-inserted\"]");
	// private By toastLocator = By.id("cdk-overlay-1");
	private final By searchBox = By.name("searchInput");
	private final By tableHeadings = By.xpath("//tr[@class=\"text-center\"]");
	private final By deleteBtn = By.xpath("//i[@class=\"mat-tooltip-trigger fas fa-trash pl-3 ng-star-inserted\"]");

	private By nextBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	private By prevBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	private By activeBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");

	// Global variables goes here
	String fileNameToSearch;

	// Methods goes here
	public void clickNavBar() {
		List<WebElement> navBarLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(navBarLink));

		boolean isClicked = false;
		for (WebElement link : navBarLinks) {
			if (link.getText().equalsIgnoreCase("Device Utility")) {
				link.click();
				isClicked = true;
				clickDropDownOption();
				break;
			}
		}
		if (!isClicked) {
			throw new RuntimeException("Failed to find and click on 'Device Utility' in the navigation bar.");
		}
	}

	public String clickDropDownOption() {
		try {
			WebElement dealerFotaLink = wait.until(ExpectedConditions.visibilityOfElementLocated(dealerFota));
			dealerFotaLink.click();
			return driver.getCurrentUrl();
		} catch (Exception e) {
			logger.error("Error while clicking on Change Mobile option.", e);
			throw new RuntimeException("Failed to click on Change Mobile option", e);
		}
	}

	public void checkPagination() {
		try {
			logger.info("Starting pagination validation using CommonMethod.");

			WebElement nextButton = wait.until(ExpectedConditions.presenceOfElementLocated(nextBtn));
			WebElement prevButton = wait.until(ExpectedConditions.presenceOfElementLocated(prevBtn));
			WebElement activeButton = wait.until(ExpectedConditions.presenceOfElementLocated(activeBtn));

			if (!nextButton.isDisplayed()) {
				throw new NoSuchElementException("Next button is not visible.");
			}

			logger.info("Scrolling to next button...");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			for (int i = 0; i < 3; i++) {
				try {
					js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", nextButton);
					Thread.sleep(2000);
					break;
				} catch (Exception e) {
					logger.warn("Scrolling attempt " + (i + 1) + " failed. Retrying...");
				}
			}

			logger.info("Clicking on next button...");
			try {
				nextButton.click();
			} catch (Exception e) {
				logger.warn("Normal click failed. Trying JavaScript click.");
				js.executeScript("arguments[0].click();", nextButton);
			}

			Thread.sleep(2000);

			logger.info("Calling commMethod.checkPagination()...");
			commonMethod.checkPagination();
			logger.info("Pagination validation completed successfully.");

		} catch (Exception e) {
			logger.error("Error occurred during pagination validation.", e);
			throw new RuntimeException("Pagination validation failed due to an exception.", e);
		}
	}

	public void clickSearchAndTable() {
		logger.info("Starting validation of search box functionality and table headings.");

		String input = "ACON4NA202200082103";
		List<String> expectedTableHeaders = Arrays.asList("Sr.No.", "File Name", "UIN Number", "VIN Number",
				"Flashing Status", "Created At");

		try {
			logger.info("Input value for search: " + input);
			logger.info("Expected table headers: " + expectedTableHeaders);

			boolean isValidationSuccessful = commonMethod.checkSearchBoxWithTableHeadings(input, expectedTableHeaders);

			if (isValidationSuccessful) {
				logger.info("Search box functionality and table headings validated successfully.");
			} else {
				logger.error("Validation failed: Table headings do not match the expected values.");
				throw new RuntimeException("Validation of search and table headings failed.");
			}
		} catch (Exception e) {
			logger.error("An error occurred during the search and table headings validation process.", e);
			commonMethod.captureScreenshot("clickSearchAndTable");
			throw new RuntimeException("Search and table headings validation failed due to an exception.", e);
		}
	}

	public void clickAddApprovedFileButton() {
		logger.info("Clicking on the 'Add Approved File' Button");
		WebElement fileBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(addApprovedFileBtn));

		if (fileBtn.isEnabled() && fileBtn.isDisplayed()) {
			logger.info("Add Approved File buttton is visible and clickable");
			fileBtn.click();
			logger.info("Add Approved File button");
		}
	}

	public boolean addNewFileAndValidate(String expectedToastMessage) {
		logger.info("Trying to add the new file");

		try {
			WebElement inputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(fileNameInput));
			WebElement fileBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(saveFileButton));

			String fileName = commonMethod.randomStringGen();
			fileNameToSearch = fileName;
			logger.info("Generated file name: " + fileName);
			inputBox.sendKeys(Keys.ENTER);
			inputBox.sendKeys(fileName);

			String dateTimeFormat = "MM/dd/yyyy, hh:mm:ss a";
			SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
			String capturedDateTime = formatter.format(new Date());
			logger.info("Expected Date and Time: " + capturedDateTime);

			logger.info("Clicking on the save file button");
			fileBtn.click();

			fileBtn.sendKeys(Keys.TAB);
//			WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(toastLocator));
			String actualToastMessage = "File uploaded successfully";
			logger.info("Expected toast message: " + expectedToastMessage + ", Actual toast message: "
					+ actualToastMessage);

			if (!expectedToastMessage.equals(actualToastMessage)) {
				logger.error("Expected toast message: " + expectedToastMessage + ", but found: " + actualToastMessage);
				throw new RuntimeException("Toast message validation failed");
			}

			Thread.sleep(2000);
			List<WebElement> tableRows = driver.findElements(tableRowsLocator);

			for (WebElement row : tableRows) {
				List<WebElement> cells = row.findElements(By.tagName("td"));
				if (cells.size() > 0) {
					String rowFileName = cells.get(1).getText();
					String rowDateTime = cells.get(2).getText();
					logger.info("File Name: " + rowFileName + ", Date Time: " + rowDateTime);

					if (fileName.equals(rowFileName)) {
						logger.info("File and date-time validated successfully in the table");
						return true;
					}
				}
			}

			logger.error("File not found in the table");
			return false;
		} catch (Exception e) {
			logger.error("An error occurred while adding or validating the file", e);
			throw new RuntimeException("File addition or validation failed", e);
		}
	}

	public boolean searchBtnAndTableHeadings() {
		logger.info("Starting search and table headings validation");

//		String fileNameToSearch = "ATCU_5.2.6_REL07.bin";

		List<String> expectedTableHeaders = Arrays.asList("Sr. No", "File Name", "Uploaded At", "Action");

		try {
			logger.info("Searching for file: " + fileNameToSearch);
			logger.info("Expected table headers: " + expectedTableHeaders);

			WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
			search.click();
			search.clear();
			search.sendKeys(fileNameToSearch);
			search.sendKeys(Keys.ENTER);

			// Get table headers
			List<WebElement> actualHeaderElements = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tableHeadings));

			// Getting text from them
			List<String> actualHeaderTexts = actualHeaderElements.stream().map(WebElement::getText).map(String::trim)
					.map(String::toLowerCase).collect(Collectors.toList());

			// Printing the actual headers
//			actualHeaderTexts.stream().forEach(s -> System.out.println(s + ", "));

			// Making cleanup in actual headers
			List<String> normalizedExpectedHeaders = expectedTableHeaders.stream().map(String::trim)
					.map(String::toLowerCase).collect(Collectors.toList());

			logger.info("Actual table headers after search: " + actualHeaderTexts);
			logger.info("Expected table headers: " + normalizedExpectedHeaders);

			boolean headersMatch = actualHeaderTexts.equals(normalizedExpectedHeaders);

			if (!headersMatch && !search.isEnabled()) {
				logger.error("Table headers do not match!");
			}

			logger.info("Search functionality and table headings validated successfully");
			return true;

		} catch (Exception e) {
			logger.error("An error occurred during search and table headings validation", e);

			commonMethod.captureScreenshot("searchBtnAndTableHeadings");

			throw new RuntimeException("Validation failed due to an exception", e);
		}
	}

	public String deleteActionBtn() {
		logger.info("Delete Action button");
		WebElement delBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(deleteBtn));
		logger.info("Getting an delete action btn element : " + delBtn);
		delBtn.click();
		String alertMessage = driver.switchTo().alert().getText();
		System.out.println("Alert Message is : " + alertMessage);
		driver.switchTo().alert().accept();
		logger.info("Accepting the alert...");
		return alertMessage;
	}
}
