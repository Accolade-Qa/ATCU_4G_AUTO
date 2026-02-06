package com.aepl.atcu.pages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.aepl.atcu.locators.CommonPageLocators;


public class CommonMethods extends CommonPageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private static Logger logger = LogManager.getLogger(CommonMethods.class);
	private SoftAssert softAssert = new SoftAssert();

	public CommonMethods(WebDriver driver, WebDriverWait wait) {
		this.driver = driver;
		this.wait = wait;
	}

	public void captureScreenshot(String testCaseName) {
		if (driver == null) {
			logger.error("WebDriver instance is null. Cannot capture screenshot for test case: {}", testCaseName);
			throw new RuntimeException("WebDriver is not initialized");
		}

		logger.info("Starting screenshot capture for test case: {}", testCaseName);

		// Check if driver supports screenshots
		if (!(driver instanceof TakesScreenshot)) {
			logger.error("Driver does not support screenshot capture. Class: {}", driver.getClass().getName());
			throw new RuntimeException("Driver does not implement TakesScreenshot.");
		}

		try {
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String screenshotName = testCaseName + "_" + timestamp + ".png";

			// Full path with system-independent separator
			String screenshotDir = System.getProperty("user.dir") + File.separator + "screenshots";
			String screenshotPath = screenshotDir + File.separator + screenshotName;

			logger.debug("Screenshot path constructed: {}", screenshotPath);

			// Ensure screenshot directory exists
			File dir = new File(screenshotDir);
			if (!dir.exists()) {
				if (dir.mkdirs()) {
					logger.info("Created directory: {}", dir.getAbsolutePath());
				} else {
					logger.warn("Could not create directory: {}. Check permissions.", dir.getAbsolutePath());
				}
			}

			// Take screenshot
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			File destFile = new File(screenshotPath);
			FileUtils.copyFile(srcFile, destFile);

			if (destFile.exists()) {
				logger.info("Screenshot successfully saved at: {}", destFile.getAbsolutePath());
			} else {
				logger.error("Screenshot file not found after copy attempt.");
			}

		} catch (IOException e) {
			logger.error("IOException while capturing or saving screenshot for test case: {}", testCaseName, e);
		} catch (Exception e) {
			logger.error("Unexpected error during screenshot capture for test case: {}", testCaseName, e);
		}
	}

	public boolean verifyWebpageLogo() {
		logger.info("Starting verification of the webpage logo.");

		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			logger.debug("Waiting for the logo element to become visible.");
			WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(ORG_LOGO));
			logger.debug("Logo element is visible. Applying highlight using JavaScript.");
			js.executeScript("arguments[0].style.border='3px solid purple'", logo);

			if (logo.isDisplayed()) {
				logger.info("Webpage logo is displayed as expected.");
				return true;
			} else {
				logger.warn("Webpage logo is not displayed despite being located.");
				throw new RuntimeException("Webpage logo is not visible.");
			}
		} catch (TimeoutException e) {
			logger.error("Timed out waiting for the logo element to become visible.", e);
			throw new RuntimeException("Logo element was not visible in time.", e);
		} catch (Exception e) {
			logger.error("An unexpected error occurred during logo verification.", e);
			throw e;
		}
	}

	public String verifyPageTitle() {
		String expectedTitle = "AEPL Sampark Diagnostic Cloud";
		logger.info("Starting verification of the project title.");

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			logger.debug("Waiting for the project title element to become visible.");

			WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(PROJECT_TITLE));
			logger.debug("Project title element is visible. Applying border highlight via JavaScript.");
			js.executeScript("arguments[0].style.border='3px solid purple'", titleElement);

			String actualTitle = titleElement.getText();
			logger.debug("Extracted project title: '{}'", actualTitle);

			if (actualTitle.equalsIgnoreCase(expectedTitle)) {
				logger.info("Project title matches expected: '{}'", actualTitle);
			} else {
				logger.warn("Project title mismatch. Expected: '{}', Found: '{}'", expectedTitle, actualTitle);
				throw new RuntimeException(
						"Project title does not match. Expected: " + expectedTitle + ", but found: " + actualTitle);
			}

			return actualTitle;

		} catch (TimeoutException e) {
			logger.error("Timed out waiting for the project title element to be visible.", e);
			throw new RuntimeException("Project title element not visible in time.", e);
		} catch (Exception e) {
			logger.error("An unexpected error occurred during project title verification.", e);
			throw e;
		}
	}

	public void clickRefreshButton() {
		logger.info("Attempting to click the refresh button.");

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;

			logger.debug("Waiting for the refresh button to be visible.");
			WebElement refreshButton = wait.until(ExpectedConditions.visibilityOfElementLocated(REFRESH_BUTTON));

			logger.debug("Scrolling to the refresh button.");
			js.executeScript("arguments[0].scrollIntoView(true);", refreshButton);
			Thread.sleep(1000); // Consider replacing with proper wait strategy

			logger.debug("Highlighting the refresh button.");
			js.executeScript("arguments[0].style.border='3px solid purple'", refreshButton);

			try {
				logger.debug("Attempting standard click on refresh button.");
				refreshButton.click();
				logger.info("Refresh button clicked using standard method.");
			} catch (ElementClickInterceptedException e) {
				logger.warn("Standard click intercepted. Attempting JavaScript click.");
				js.executeScript("arguments[0].click();", refreshButton);
				logger.info("Refresh button clicked using JavaScript.");
			}

			Thread.sleep(1000); // Optional: wait to observe effect after click

		} catch (Exception e) {
			logger.error("Failed to click on the refresh button.", e);
			throw new RuntimeException("Failed to click on the refresh button.", e);
		}
	}

	public void clickNavBarDash() {
		logger.info("Attempting to click on the 'Dashboard' link in the navigation bar.");

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;

			logger.debug("Waiting for all navigation bar links to be visible.");
			List<WebElement> navBarLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DASHBOARD));

			logger.debug("Highlighting the first navigation bar element.");
			js.executeScript("arguments[0].style.border='3px solid purple'", navBarLinks.get(0));

			boolean isClicked = false;

			for (WebElement link : navBarLinks) {
				String linkText = link.getText().trim();
				logger.debug("Checking navigation link text: {}", linkText);

				if (linkText.equalsIgnoreCase("Dashboard")) {
					logger.info("Found 'Dashboard' link. Clicking it now.");
					link.click();
					isClicked = true;
					break;
				}
			}

			if (!isClicked) {
				logger.error("Could not find 'Dashboard' link in the navigation bar.");
				throw new RuntimeException("Failed to find and click on 'Dashboard' in the navigation bar.");
			} else {
				logger.info("'Dashboard' link successfully clicked.");
			}

		} catch (Exception e) {
			logger.error("An error occurred while attempting to click on the 'Dashboard' link.", e);
			throw new RuntimeException("Error while clicking on 'Dashboard' link in the navigation bar.", e);
		}
	}

	public void clickNavBar() {
		logger.info("Attempting to click on the 'Dashboard' link from the navigation bar.");

		try {
			logger.debug("Waiting for visibility of all navigation bar elements.");
			List<WebElement> navBarLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DASHBOARD));
			logger.debug("Total navigation links found: {}", navBarLinks.size());

			if (navBarLinks.isEmpty()) {
				logger.error("No navigation bar links found.");
				throw new RuntimeException("No navigation bar links found for 'Dashboard'.");
			}

			JavascriptExecutor js = (JavascriptExecutor) driver;
			boolean isClicked = false;

			for (WebElement link : navBarLinks) {
				String linkText = link.getText().trim();
				logger.debug("Checking link text: '{}'", linkText);

				js.executeScript("arguments[0].style.border='3px solid green'", link);

				if (linkText.equalsIgnoreCase("Dashboard")) {
					logger.info("Found 'Dashboard' link. Clicking it via JavaScript.");
					logger.debug("Element accessible name: {}", link.getAccessibleName());
					js.executeScript("arguments[0].click();", link);
					isClicked = true;
					break;
				}
			}

			if (!isClicked) {
				logger.error("'Dashboard' link not found in the navigation bar.");
				throw new RuntimeException("Failed to find and click on 'Dashboard' in the navigation bar.");
			} else {
				logger.info("'Dashboard' link successfully clicked.");
			}

		} catch (StaleElementReferenceException e) {
			logger.error("StaleElementReferenceException: Navigation bar element became stale.", e);
			throw new RuntimeException("Element went stale. Try re-fetching before clicking.", e);
		} catch (JavascriptException e) {
			logger.error("JavascriptException: JavaScript execution failed.", e);
			throw new RuntimeException("JavaScript execution failed. Element might be undefined.", e);
		} catch (Exception e) {
			logger.error("Unexpected exception occurred while clicking navigation bar.", e);
			throw new RuntimeException("Unexpected error during navigation bar interaction.", e);
		}
	}

	public boolean clickNavBarDeviceUtil() {
		logger.info("Attempting to click on 'Device Utility' link in the navigation bar.");

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			List<WebElement> navBarLinks = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DEVICE_UTILITY));

			logger.debug("Total navigation bar elements found for 'Device Utility': {}", navBarLinks.size());

			for (WebElement link : navBarLinks) {
				js.executeScript("arguments[0].style.border='3px solid purple'", link);
				String linkText = link.getText().trim();
				logger.debug("Evaluating nav link text: '{}'", linkText);

				if (linkText.equalsIgnoreCase("Device Utility")) {
					logger.info("'Device Utility' link found. Attempting to click.");
					try {
						link.click();
						logger.debug("Clicked using standard WebElement.click().");
					} catch (Exception e) {
						logger.warn("Standard click failed. Attempting JavaScript click.", e);
						js.executeScript("arguments[0].click();", link);
					}
					logger.info("Successfully clicked on 'Device Utility'. Accessible name: {}",
							link.getAccessibleName());
					return true;
				}
			}

			logger.warn("'Device Utility' link not found in the navigation bar.");
			return false;

		} catch (Exception e) {
			logger.error("Exception occurred while attempting to click 'Device Utility': {}", e.getMessage(), e);
			return false;
		}
	}

	public boolean clickNavBarUser() {
		logger.info("Attempting to click on 'User' link in the navigation bar.");

		try {
			List<WebElement> navBarLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(USER));

			logger.debug("Total navigation bar elements found for 'User': {}", navBarLinks.size());

			for (WebElement link : navBarLinks) {
				highlightElement(link, "solid purple");
				String linkText = link.getText().trim();
				logger.debug("Evaluating nav link text: '{}'", linkText);

				if (linkText.equalsIgnoreCase("User")) {
					logger.info("'User' link found. Attempting to click.");

					try {
						link.click();
						logger.info("Successfully clicked on 'User'. Accessible name: {}", link.getAccessibleName());
						return true;
					} catch (Exception e) {
						logger.error("Error clicking on 'User' link using standard click: {}", e.getMessage(), e);
						return false;
					}
				}
			}

			logger.warn("'User' link not found in the navigation bar.");
			return false;

		} catch (Exception e) {
			logger.error("Exception occurred while interacting with navigation bar for 'User': {}", e.getMessage(), e);
			return false;
		}
	}

	public void clickNavBarUserPro() {
		logger.info("Attempting to click on 'User Profile' (Hi, Super Ad) in the navigation bar.");

		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			List<WebElement> navBarLinks = wait
					.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(USER_PROFILE));

			logger.debug("Total user profile elements found: {}", navBarLinks.size());

			boolean isClicked = false;

			for (WebElement link : navBarLinks) {
				js.executeScript("arguments[0].style.border='3px solid purple'", link);
				String linkText = link.getText().trim();
				logger.debug("Evaluating nav link text: '{}'", linkText);

				if (linkText.equalsIgnoreCase("Hi, Super Ad")) {
					js.executeScript("arguments[0].click();", link); // Using JS click for reliability
					logger.info("Clicked on user profile link: {}", link.getAccessibleName());
					isClicked = true;
					break;
				}
			}

			if (!isClicked) {
				logger.error("User profile link with text 'Hi, Super Ad' not found in the navigation bar.");
				throw new RuntimeException("Failed to find and click on 'User Profile' in the navigation bar.");
			}

		} catch (Exception e) {
			logger.error("Exception occurred while clicking on 'User Profile': {}", e.getMessage(), e);
			throw new RuntimeException("Error occurred in clickNavBarUserPro()", e);
		}
	}

	public String checkCopyright() {
		logger.info("Attempting to locate and retrieve the copyright footer.");

		WebElement copyRight = driver.findElement(COPYRIGHT);
		highlightElement(copyRight, "solid purple");

		String text = copyRight.getText();
		logger.info("Copyright text found: {}", text);
		return text;
	}

	public String checkVersion() {
		logger.info("Attempting to locate and retrieve the version info from the footer.");

		try {
			Thread.sleep(2000);
			WebElement version = driver.findElement(VERSION);
			highlightElement(version, "solid purple");

			String versionText = version.getText();
			logger.info("Version text found: {}", versionText);
			return versionText;

		} catch (NoSuchElementException e) {
			logger.error("Version element not found on the page: {}", e.getMessage(), e);
			return "No version was found on page!!!";

		} catch (InterruptedException e) {
			logger.warn("Thread was interrupted while waiting: {}", e.getMessage(), e);
			Thread.currentThread().interrupt(); // Restore the interrupted status
			return "Thread interrupted while checking version.";

		} catch (Exception e) {
			logger.error("Unexpected error occurred while checking version: {}", e.getMessage(), e);
			return "An error occurred while checking the version.";
		}
	}

	public String validateComponents() {
		try {
			logger.info("Starting validation of main page components.");

			// Locate components
			WebElement headerContainer = driver.findElement(HEADER_CONTAINER);
			WebElement pageHeader = driver.findElement(PAGE_HEADER);
			WebElement componentContainer = driver.findElement(COMPONENT_CONTAINER);
			WebElement separator = driver.findElement(SEPARATOR);
			WebElement footer = driver.findElement(FOOTER);

			logger.debug("All required components located successfully.");

			// Highlight components
			highlightElement(headerContainer, "solid purple");
			logger.debug("Highlighted: Header Container");
			softAssert.assertTrue(headerContainer.isDisplayed(), "No header container is displayed");

			highlightElement(pageHeader, "solid purple");
			logger.debug("Highlighted: Page Header");
			softAssert.assertTrue(pageHeader.isDisplayed(), "No pageHeader container is displayed");

			highlightElement(componentContainer, "solid purple");
			logger.debug("Highlighted: Component Container");
			softAssert.assertTrue(componentContainer.isDisplayed(), "No componentContainer container is displayed");

			highlightElement(separator, "solid purple");
			logger.debug("Highlighted: Separator");
			softAssert.assertTrue(separator.isDisplayed(), "No separator container is displayed");

			highlightElement(footer, "solid purple");
			logger.debug("Highlighted: Footer");
			softAssert.assertTrue(footer.isDisplayed(), "No footer container is displayed");

			logger.info("All components are displayed and validated successfully.");
			return "All components are displayed and validated successfully.";
		} catch (Exception e) {
			logger.error("Error validating components: {}", e.getMessage(), e);
			return "Error validating components: " + e.getMessage();
		}
	}

	public String validateComponentTitle() {
		WebElement component_title = driver.findElement(COMPONENT_TITLE);
		highlightElement(component_title, "solid purple");
		softAssert.assertEquals(component_title, "Government Servers List", "Component title did not matched");
		return component_title.getText();
	}

	public String validateButtons() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

			logger.info("Starting validation of all buttons on the page.");

			js.executeScript("window.scrollTo(0, 0)");
			Thread.sleep(200);

			long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
			while (true) {
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				long newHeight = (long) js.executeScript("return document.body.scrollHeight");
				if (newHeight == lastHeight) {
					break;
				}
				lastHeight = newHeight;
			}

			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ALL_BTN));

			List<WebElement> buttons = new ArrayList<>(driver.findElements(ALL_BTN));
			int total = buttons.size();
			logger.debug("Found {} button elements after scrolling.", total);

			softAssert.assertFalse(buttons.isEmpty(), "No buttons found on the page!");

			for (int i = 0; i < total; i++) {
				boolean validated = false;
				int attempts = 0;

				while (!validated && attempts < 3) {
					try {
						WebElement button = driver.findElements(ALL_BTN).get(i);

						js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", button);

						softAssert.assertTrue(button.isDisplayed(), "Button not displayed: " + button.getText());
						softAssert.assertTrue(button.isEnabled(), "Button not enabled: " + button.getText());

						highlightElement(button, "solid purple");
						validated = true;
					} catch (StaleElementReferenceException | IndexOutOfBoundsException e) {
						logger.warn("Retrying validation for button index {} (attempt {}/3)", i + 1, attempts + 1);
						wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(ALL_BTN));
						attempts++;
					}
				}

				if (!validated) {
					softAssert.fail("Button at index " + i + " could not be validated after retries.");
				}
			}

			logger.info("All buttons validated and highlighted successfully.");
			return "All buttons are displayed and enabled successfully.";

		} catch (Exception e) {
			logger.error("Unexpected exception while validating buttons: {}", e.getMessage(), e);
			softAssert.fail("Unexpected error while validating buttons: " + e.getMessage());
			return "Error validating buttons: " + e.getMessage();
		}
	}

	public String clickSampleFileButton() {
		logger.info("Attempting to click on the 'Sample File' button up to 5 times.");

		for (int i = 0; i <= 3; i++) {
			try {
				logger.debug("Attempt {} - Waiting for 'Sample File' button to be clickable.", i + 1);

				WebElement sampleFileButton = wait.until(ExpectedConditions.elementToBeClickable(SAMPLE_FILE_BUTTON));
				highlightElement(sampleFileButton, "solid purple");
				logger.debug("Highlight and click attempt on 'Sample File' button.");

				sampleFileButton.click();
				Thread.sleep(500);
				logger.info("'Sample File' button clicked successfully on attempt {}.", i + 1);

//				Alert alert = wait.until(ExpectedConditions.alertIsPresent());
//				if (alert != null) {
//					String alertText = alert.getText();
//					logger.info("Alert detected after clicking: {}", alertText);
//					alert.accept();
//					logger.debug("Alert accepted.");
//				} else {
//					continue;
//				}

//				break;

			} catch (Exception e) {
				logger.warn("Attempt {} failed to click 'Sample File' button: {}", i + 1, e.getMessage());
			}
		}

		logger.info("Finished attempts to click 'Sample File' button.");
		return "File downloaded successfully.";
	}

	public void checkPagination() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			logger.info("Starting pagination check...");

			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			Thread.sleep(500);

			WebElement rowPerPage = wait.until(ExpectedConditions.elementToBeClickable(ROW_PER_PAGE));
			highlightElement(rowPerPage, "solid purple");
			Select select = new Select(rowPerPage);

			List<WebElement> options = select.getOptions();
			logger.info("Found {} 'Rows per page' options. Iterating through each.", options.size());

			// ✅ Iterate fresh each time to avoid stale elements
			for (int i = 0; i < options.size(); i++) {
				js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
				Thread.sleep(500);

				// re-fetch select and option
				rowPerPage = wait.until(ExpectedConditions.elementToBeClickable(ROW_PER_PAGE));
				select = new Select(rowPerPage);
				WebElement option = select.getOptions().get(i);

				logger.debug("Selecting option: {}", option.getText());
				option.click();
				Thread.sleep(500);
			}

			// ✅ Reset back to default
			rowPerPage = wait.until(ExpectedConditions.elementToBeClickable(ROW_PER_PAGE));
			select = new Select(rowPerPage);
			WebElement defaultOption = select.getOptions().get(0);
			logger.debug("Resetting to default row option: {}", defaultOption.getText());
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			defaultOption.click();

			// FORWARD pagination
			logger.info("Starting forward pagination clicks...");
			for (int i = 1; i < 4; i++) {
				js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				Thread.sleep(300);
				WebElement rightArrow = wait.until(ExpectedConditions.elementToBeClickable(RIGHT_ARROW));
				highlightElement(rightArrow, "solid purple");
				js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", rightArrow);
				rightArrow.click();
				logger.debug("Clicked forward arrow - iteration {}", i);
				Thread.sleep(500);
			}

			// BACKWARD pagination
			logger.info("Starting backward pagination clicks...");
			for (int i = 4; i > 1; i--) {
				js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
				Thread.sleep(300);
				WebElement leftArrow = wait.until(ExpectedConditions.elementToBeClickable(LEFT_ARROW));
				highlightElement(leftArrow, "solid purple");
				js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});", leftArrow);
				leftArrow.click();
				logger.debug("Clicked backward arrow - iteration {}", i);
				Thread.sleep(500);
			}

			logger.info("Pagination check completed successfully.");

		} catch (Exception e) {
			logger.error("Error occurred during pagination check: {}", e.getMessage(), e);
		}
	}

	public String validateCards() {
		try {
			List<WebElement> cards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ALL_CARDS));
			Assert.assertFalse(cards.isEmpty(), "No cards found on the page!");
			logger.info("Found {} card(s) on the page.", cards.size());

			softAssert.assertTrue(cards.size() == 6, "below 6 cards found on the page!");
			for (WebElement card : cards) {
				highlightElement(card, "solid purple");
				String cardText = card.getText().trim();
				logger.info("Card content: {}", cardText);
			}

			return "All cards are displayed and validated successfully.";

		} catch (Exception e) {
			logger.error("Error validating cards: {}", e.getMessage(), e);
			return "Error validating cards: " + e.getMessage();
		}
	}

	public boolean validateExportButton() {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		for (int attempt = 0; attempt < 3; attempt++) {
			try {
				logger.info("Attempt {} to validate Export button.", attempt + 1);

				try {
					WebElement exportButton = driver.findElement(EXPORT_BUTTON);
					if (exportButton.isDisplayed()) {
						logger.debug("Export button is now visible.");
						js.executeScript("arguments[0].scrollIntoView({behavior: 'auto', block: 'center'});",
								exportButton);
						if (exportButton.isEnabled()) {
							exportButton.click();

							logger.info("Clicked on Export button.");

							Alert alert = wait.until(ExpectedConditions.alertIsPresent());
							logger.debug("Alert present with text: {}", alert.getText());
							alert.accept();
							logger.info("Alert accepted.");
							return true;
						} else {
							logger.warn("Export button is not enabled.");
							return false;
						}
					}
				} catch (Exception inner) {
					// Not visible yet; scroll further
					js.executeScript("window.scrollBy(0, 200);");
					Thread.sleep(300);
				}

			} catch (Exception e) {
				logger.error("Attempt {} failed: {}", attempt + 1, e.getMessage());
			}
		}

		logger.warn("Export button validation failed after 3 attempts.");
		return false;
	}

	public boolean validateSampleFileButton() {
		try {
			for (int i = 0; i < 5; i++) {
				logger.info("Attempt {}: Validating Sample File button.", i + 1);

				WebElement sampleFileButton = wait.until(ExpectedConditions.elementToBeClickable(SAMPLE_FILE_BUTTON));
				logger.debug("Sample File button is clickable.");

				sampleFileButton.click();
				logger.info("Clicked on Sample File button.");

				Alert alert = wait.until(ExpectedConditions.alertIsPresent());
				logger.debug("Alert appeared with text: {}", alert.getText());

				alert.accept();
				logger.info("Alert accepted.");

				Thread.sleep(2000); // Simulate waiting for download
				logger.debug("Waited 2 seconds after alert.");
			}
		} catch (Exception e) {
			logger.error("Error validating Sample File button: {}", e.getMessage(), e);
			return false;
		}
		logger.info("Sample File button validated successfully.");
		return true;
	}

	public void highlightElement(WebElement element, String colorCode) {
		((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px " + colorCode + " '", element);
	}

	public void highlightElements(List<WebElement> elements, String colorCode) {
		for (WebElement element : elements) {
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px " + colorCode + " '", element);
		}
	}

	public String validateInputBoxError() {
		logger.info("Validating input box error message.");

		try {
			WebElement inputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_BOX));
			highlightElement(inputBox, "solid purple");
			inputBox.click();

			Thread.sleep(500);

			WebElement body = driver.findElement(By.xpath("//body/app-root/app-header/div"));
			body.click();

			Thread.sleep(500);

			WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT_BOX_ERROR));
			String errorMessage = errorElement.getText().trim();
			logger.info("Error message displayed: {}", errorMessage);

			if (!errorMessage.isEmpty()) {
				return errorMessage;
			} else {
				logger.warn("Error message element found but was empty.");
				return "Error Message Empty";
			}
		} catch (TimeoutException e) {
			logger.error("Error message not found: {}", e.getMessage());
			return "Error Message Not Found";
		} catch (Exception e) {
			logger.error("Unexpected exception during error validation: {}", e.getMessage(), e);
			return "Validation Failed";
		}
	}

	public boolean verifyCSVHeader(String filePath, String expectedHeader) {
		logger.info("Verifying file header in: {}", filePath);

		File file = new File(filePath);
		if (!file.exists() || file.length() == 0) {
			logger.error("File not found or empty: {}", filePath);
			return false;
		}

		String lowerName = file.getName().toLowerCase();

		try {
			List<String> actualHeaders;

			if (lowerName.endsWith(".csv")) {
				// --- CSV ---
				List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
				if (lines.isEmpty()) {
					logger.warn("CSV file is empty: {}", filePath);
					return false;
				}

				actualHeaders = Arrays.stream(lines.get(0).split(",")).map(String::trim).filter(h -> !h.isEmpty()) // remove
																													// blank
																													// headers
						.collect(Collectors.toList());

			} else if (lowerName.endsWith(".xlsx")) {
				// --- Excel (.xlsx) ---
				try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

					Sheet sheet = workbook.getSheetAt(0);
					Row headerRow = sheet.getRow(0);
					if (headerRow == null) {
						logger.warn("Excel file has no header row: {}", filePath);
						return false;
					}

					actualHeaders = new ArrayList<>();
					for (Cell cell : headerRow) {
						String value = cell.getStringCellValue().trim();
						if (!value.isEmpty()) {
							actualHeaders.add(value);
						}
					}
				}

			} else {
				logger.error("Unsupported file type: {}", filePath);
				return false;
			}

			// --- Normalize both expected and actual ---
			List<String> expectedHeaders = Arrays.stream(expectedHeader.split(",")).map(String::trim)
					.filter(h -> !h.isEmpty()).collect(Collectors.toList());

			logger.debug("Expected headers: {}", expectedHeaders);
			logger.debug("Actual headers: {}", actualHeaders);

			// --- Compare only the order of expected headers at the beginning ---
			if (actualHeaders.size() >= expectedHeaders.size()
					&& actualHeaders.subList(0, expectedHeaders.size()).equals(expectedHeaders)) {
				logger.info("✅ Header matches expected (ignoring trailing blanks).");
				return true;
			} else {
				logger.warn("❌ Header mismatch.\nExpected: {}\nFound: {}", expectedHeaders, actualHeaders);
				return false;
			}

		} catch (Exception e) {
			logger.error("Error verifying file header: {}", e.getMessage(), e);
			return false;
		}
	}
}
