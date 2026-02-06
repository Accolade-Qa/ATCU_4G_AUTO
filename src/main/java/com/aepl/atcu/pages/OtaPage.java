package com.aepl.atcu.pages;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.atcu.actions.CalendarActions;
import com.aepl.atcu.actions.MouseActions;

public class OtaPage extends MouseActions{

	// Global variables
	private WebDriver driver;
	private WebDriverWait wait;
	private CommonMethod commonMethod;
	private CalendarActions calendarActions;
	private MouseActions mouseActions;
  
	private static final Logger logger = LogManager.getLogger(OtaPage.class);

	// Constructor
	public OtaPage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		this.commonMethod = new CommonMethods(driver);
		this.calendarActions = new CalendarActions(driver, wait);
		this.mouseActions = new MouseActions(driver);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	// Variables
	public String batchCount = "";

	// Locators
	private By navBarLink = By.xpath("//span[@class='headers_custom color_3D5772']");
	private By otaLink = By.xpath("//a[@class='dropdown-item ng-star-inserted'][4]");
	private By buttonsList = By.xpath("//button[@class='btn btn-outline-primary ng-star-inserted']");
	private By nextBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	private By prevBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	private By activeBtn = By.xpath("//a[@class=\"ng-star-inserted\"]");
	private By eyeActionButton = By.xpath("//mat-icon[@mattooltip='View']");
	private By calendar = By.xpath("//button[@class=\"mat-focus-indicator mat-icon-button mat-button-base\"]");
	private By batchIdFrom = By.id("fromBatchId");
	private By batchIdTo = By.id("toBatchId");
	private By batchSubmitBtn = By.xpath("//button[@class=\"btn-sm btn btn-outline-primary example-full-width\"]");
	private By clearButton = By.xpath("//button[@class=\"btn-sm btn btn-outline-secondary example-full-width\"]");
	public By reportButton = By.xpath("//button[@class=\"btn-sm btn example-full-width float-right\"]");
	private By allInputFields = By.tagName("input");
	private By toastMessageOfOtaAdd = By.xpath("//simple-snack-bar//span[text()='Success']");
	private By editButtonOfOta = By.xpath("//mat-icon[@class=\"mat-icon notranslate mx-2 material-icons mat-icon-no-color\"]");
	private By deleteButtonOfOta = By.xpath(
			"//mat-icon[class=\"mat-icon notranslate mat-tooltip-trigger delete-icon material-icons mat-icon-no-color\"]");
	private By dropdownOtaType = By.id("id=\"mat-select-6\"");

	// Methods
	public void clickNavBar() {
		List<WebElement> navBarLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(navBarLink));

		for (WebElement link : navBarLinks) {
			if (link.getText().equalsIgnoreCase("Device Utility")) {
				link.click();
				return;
			}
		}
		throw new RuntimeException("Failed to find and click on 'Device Utility' in the navigation bar.");
	}

	public String clickDropdown() {
		try {
			WebElement changeMobileLink = wait.until(ExpectedConditions.visibilityOfElementLocated(otaLink));
			changeMobileLink.click();
			return driver.getCurrentUrl();
		} catch (Exception e) {
			logger.error("Error while clicking on Change Mobile option.", e);
			throw new RuntimeException("Failed to click on Change Mobile option", e);
		}
	}

	public void checkButtons() {
		try {
			List<WebElement> btnList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(buttonsList));
			logger.info("Found " + btnList.size() + " buttons on the page.");

			for (int i = 0; i < btnList.size(); i++) {
				WebElement btn = btnList.get(i);

				if (btn.isDisplayed() && btn.isEnabled()) {
					logger.info("Clicking button " + (i + 1) + " with text: " + btn.getText());
					btn.click();
					logger.info("Navigated to URL: " + driver.getCurrentUrl());
					driver.navigate().back();
					logger.info("Navigated back to the original page.");
					btnList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(buttonsList));
				} else {
					logger.warn("Button " + (i + 1) + " is either not displayed or not enabled.");
				}
			}

			logger.info("Successfully clicked all buttons on the page.");
		} catch (Exception e) {
			logger.error("An error occurred while interacting with buttons.", e);
			throw new RuntimeException("Failed to interact with all buttons.", e);
		}
	}

	//
	public boolean checkSearchBoxAndTable(String input, List<String> expectedHeaders) {
		// this is to get the latest batch number of the ota table.
		batchCount = driver.findElement(By.xpath("//table/tbody/tr[1]/td[1]")).getText();

		return commonMethod.checkSearchBoxWithTableHeadings(input, expectedHeaders);
	}

	public void checkActionButtons() {
		logger.log(Level.INFO, "Checking the eye action button");
		try {
			driver.switchTo().activeElement().findElement(By.xpath("//table/tbody/tr/td[9]"));
			commonMethod.clickEyeActionButton(eyeActionButton);
		} catch (Exception e) {
			logger.error("Error while clicking on the eye action button", e);
		}
		logger.log(Level.INFO, "Clicked on the eye action button");
	}

	public void checkPagination() {
		try {
			WebElement nextButton = wait.until(ExpectedConditions.visibilityOfElementLocated(nextBtn));
			WebElement prevButton = wait.until(ExpectedConditions.visibilityOfElementLocated(prevBtn));
			WebElement actButton = wait.until(ExpectedConditions.visibilityOfElementLocated(activeBtn));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", nextButton);

			Thread.sleep(2000);

			logger.log(Level.INFO, "checking the pagination button here");
			commonMethod.checkPagination(nextButton, prevButton, actButton);
			logger.log(Level.INFO, "log after checking the pagination");
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void clickOtaBatchReport() {
		// checking the ota batch report button
		try {
			driver.navigate().refresh();

			List<WebElement> buttonList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(buttonsList));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", buttonList.get(0));

			Thread.sleep(2000);

			for (WebElement button : buttonList) {
				if (button.getText().equalsIgnoreCase("OTA Batch Report")) {
					button.click();
					logger.info("Clicked on the OTA Batch Report button");
					System.out.println("Navigated to URL: " + driver.getCurrentUrl());
					return;
				}
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void getOtaBatchDateWise() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(calendar));
			WebElement fromBatch = wait.until(ExpectedConditions.visibilityOfElementLocated(batchIdFrom));
			WebElement toBatch = wait.until(ExpectedConditions.visibilityOfElementLocated(batchIdTo));
			WebElement submitBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(batchSubmitBtn));
			WebElement clearBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(clearButton));

			for (int i = 0; i < 4; i++) {
				// From Date
				calendarActions.selectDate(calendar, "31-01-2025");
				logger.info("Selected the date from the calendar");
				
				// To Date
				calendarActions.selectDate(calendar, "03-02-2025");
				logger.info("Selected the date from the calendar");

				int toCount = Integer.parseInt(batchCount) - 20;

				Thread.sleep(1000);
				fromBatch.click();
				fromBatch.sendKeys(String.valueOf(toCount));

				Thread.sleep(1000);
				toBatch.click();
				toBatch.sendKeys(String.valueOf(batchCount));

				// After the loop end submitting the value
				if (i == 1) {
					Thread.sleep(1000);
					submitBtn.click();
					break;
				}
				// in first iteration press on clear
				Thread.sleep(1000);
				clearBtn.click();
			}

		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void checktableHeading() {
		logger.info("Starting to check table headings.");

		List<String> expectedHeadings = Arrays.asList("Batch ID", "Batch Type", "Batch Date", "Total Device Uploaded",
				"Total Device Completed", "Total Device Aborted", "Total Device In-progress/Pending",
				"Batch Percentage");

		logger.info("Expected table headings: " + expectedHeadings);

		boolean result = commonMethod.checkTableHeadings(expectedHeadings);

		if (result) {
			logger.info("Table headings are as expected.");
		} else {
			logger.warn("Table headings do not match the expected values.");
		}
	}

	public boolean checkReportsButtons() {
		logger.info("Starting to check report buttons.");

		try {
			List<WebElement> reportButtons = wait
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(reportButton));
			logger.info("Found " + reportButtons.size() + " report buttons.");

			for (WebElement button : reportButtons) {
				if (button.isEnabled() && button.isDisplayed()) {
					Thread.sleep(2000);
					System.out.println("Button text: " + button.getText());
					if (button.getText().contains("Batch Summary Report")) {
						commonMethod.reportDownloadButtons(button);
						return true;
					} else {
						logger.warn("Button text does not match the expected value.");
					}
				} else {
					logger.warn("Button is either not enabled or not displayed.");
				}
			}
		} catch (Exception e) {
			logger.error("An error occurred while checking report buttons.", e);
		}

		logger.info("Finished checking report buttons. No clickable button found.");
		driver.navigate().back();
		return false;
	}

	public String clickOtaMaster() {
		try {
			driver.navigate().back();
			Thread.sleep(1000);
			List<WebElement> buttonList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(buttonsList));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", buttonList.get(1));

			Thread.sleep(1000);

			for (WebElement button : buttonList) {
				if (button.getText().equalsIgnoreCase("OTA Master")) {
					button.click();
					logger.info("Clicked on the OTA Master button");
					System.out.println("Navigated to URL: " + driver.getCurrentUrl());
					return driver.getCurrentUrl();
				}
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return "link not found";
	}
	
	// all other methods here
	public void addNewOta() {
		// Add all types of all ota one by one like 
		/*
		 *  *SET#Example# /
		 	*SET#Example#VAL# /
		 	*SET#Example#VAL#VAL /
		 	*GET#Example# /
		 	*CLR#Example#
		 * */
	}
	
	public void checkSearchAndTable() {
		// step 1: search ota that is added
		// step 2: check the table
		
	}

	public String fillAndSubmitOtaForm(String action) {
		WebElement toastConfirmation;
		List<WebElement> inputFields = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(allInputFields));

		try {
			// Fill input fields dynamically based on placeholders
			for (WebElement inputField : inputFields) {
				String placeholder = inputField.getDomAttribute("placeholder");
				if (placeholder == null)
					continue;

				switch (placeholder) {
				case "Please Enter OTA Command Name":
					inputField.sendKeys("DEMO");
					Thread.sleep(500);
					break;
				case "Please Enter OTA Command":
					inputField.sendKeys("*GET#CIIP1#");
					Thread.sleep(500);
					break;
				case "Please Enter Keyword":
					inputField.sendKeys("CIIP1");
					Thread.sleep(500);
					break;
				case "Please Enter Example":
					inputField.sendKeys("*GET#Example#");
					Thread.sleep(500);
					break;
				case "Please Enter Min Value":
					inputField.sendKeys("0");
					Thread.sleep(500);
					break;
				case "Please Enter Max Value":
					inputField.sendKeys("1");
					Thread.sleep(500);
					break;
				default:
					logger.warn("Unknown placeholder: " + placeholder);
				}
			}

			// Locate buttons dynamically
			By addButtonLocator = By.xpath("//button[@class='btn btn-outline-primary ng-star-inserted']");
			By updateButtonLocator = By.xpath("//button[@class=\"btn btn-outline-primary ml-1 ng-star-inserted\"]");

			boolean isAddButtonPresent = driver.findElements(addButtonLocator).size() > 0;
			boolean isUpdateButtonPresent = driver.findElements(updateButtonLocator).size() > 0;

			if ("add".equalsIgnoreCase(action) && isAddButtonPresent) {
				WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(addButtonLocator));
				addButton.click();
				toastConfirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessageOfOtaAdd));
				return toastConfirmation.getText();

			} else if ("update".equalsIgnoreCase(action) && isUpdateButtonPresent) {
				WebElement updateButton = wait.until(ExpectedConditions.elementToBeClickable(updateButtonLocator));
				updateButton.click();
				toastConfirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessageOfOtaAdd));
				return toastConfirmation.getText();
			} else {
				logger.warn("No valid button found to click for action: " + action);
				return "No valid button found";
			}

		} catch (Exception e) {
			logger.error("An error occurred while filling and submitting the OTA form.", e);
			return "Error occurred";
		}
	}

	// Extra writed method.
	public boolean checkSearchAndTableOfOtaMaster() {
		String searchInput = "*GET#CIIP1#";
		List<String> expectedHeaders = Arrays.asList("OTA Command Name", "OTA Command", "Keyword", "Example", "Min",
				"Max", "Action");
		return commonMethod.checkSearchBoxWithTableHeadings(searchInput, expectedHeaders);
	}

	public void checkOtaMasterActionButtons() {
		String searchInput = "CIIP1";
		try {
			commonMethod.checkSearchBox(searchInput);

			WebElement editButton = wait.until(ExpectedConditions.visibilityOfElementLocated(this.editButtonOfOta));
			editButton.click();

			String updateMessage = fillAndSubmitOtaForm("update");
			boolean isOtaUpdate = updateMessage.contains("Successfully updated.");
			System.out.println("OTA UPDATED ? " + isOtaUpdate);

			if (isOtaUpdate) {
				System.out.println("OTA command updated successfully.");
			} else {
				System.out.println("OTA command not updated.");
			}

			// Ensure delete button is located again
			WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(deleteButtonOfOta));

			System.out.println("Attempting to move to delete button...");
			mouseActions.moveToElement(deleteButton);

			System.out.println("Attempting to click delete button...");
			mouseActions.clickElement(deleteButton);

			Alert alert = driver.switchTo().alert();
			alert.accept();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectOtaTypeDropdown() {
		// step 1: select the ota type dropdown
		WebElement otaTypeDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOtaType));
		otaTypeDropdown.click();

		// step 2: click on the ota type : ALL
		Select otaType = new Select(otaTypeDropdown);
		otaType.selectByVisibleText("ALL");

		// step 3: validate the pagination and count of ALL ota type
		// step 4: click on the ota type : SET
		// step 5: validate the pagination and count of SET ota type
		// step 6: click on the ota type : GET
		// step 7: validate the pagination and count of GET ota type
		// step 8: click on the ota type : CLR
		// step 9: validate the pagination and count of CLR ota type
		
		// Thank You !!!
		
		
	}

}
