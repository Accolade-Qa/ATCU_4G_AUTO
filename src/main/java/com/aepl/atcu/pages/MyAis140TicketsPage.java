package com.aepl.atcu.pages;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.atcu.api.model.AisTicketContext;
import com.aepl.atcu.api.service.AisTicketService;
import com.aepl.atcu.locators.MyAis140TicketsPageLocators;
import com.aepl.atcu.util.PageActionsUtil;
import com.aepl.atcu.util.RandomGeneratorUtils;

public class MyAis140TicketsPage extends MyAis140TicketsPageLocators {
	private final WebDriver driver;
	private final WebDriverWait wait;
	private final PageActionsUtil actions;
	private final RandomGeneratorUtils randomUtils;
	private final AisTicketService aisTicketService;
	private AisTicketContext apiTicketContext;
	private static final Logger logger = LogManager.getLogger(MyAis140TicketsPage.class);

	public MyAis140TicketsPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		this.actions = new PageActionsUtil(driver, this.wait);
		this.randomUtils = new RandomGeneratorUtils();
		this.aisTicketService = new AisTicketService();
	}

	public String VIN_NO = generateRandomString(17);
	public String ICCID = "89916490634628942181";
	public String Ticket_No; // Global variable to store the generated Ticket Number

	public String GenerateToken() {
		return aisTicketService.generateToken();
	}

// Locators Goes here

	public String generateRandomString(int length) {
		return randomUtils.generateRandomString(length);

	}

	// Utility method to generate a random date in "yyyy-MM-dd" format
	public String generateRandomDate() {
		Random random = new Random();
		int year = random.nextInt(10) + 2020; // Random year between 2020 and 2029
		int month = random.nextInt(12) + 1; // Random month between 1 and 12
		int day = random.nextInt(28) + 1; // Random day between 1 and 28 (to avoid issues with different month lengths)

		// Create a Calendar instance and set the random year, month, and day
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1); // Calendar months are 0-based (0 for January)
		calendar.set(Calendar.DAY_OF_MONTH, day);

		// Format the date in "yyyy-MM-dd" format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(calendar.getTime());
	}

	// Utility method to generate a random year
	public int generateRandomYear(int lenghth) {
		Random random = new Random();
		return random.nextInt(10) + 2020; // Random year between 2020 and 2029

	}

	// Utility method to generate a random number within a specified range
	public String generateRandomNumber(int length) {
		return randomUtils.generateRandomNumber(length);
	}

	public int generateRandomSingleDigit() {
		Random random = new Random();
		return random.nextInt(10); // Returns a number between 0 and 9
	}

	public LocalDate generateYesterdayDate() {
		LocalDate today = LocalDate.now(); // Get today's date
		LocalDate yesterday = today.minusDays(1); // Subtract one day
		return yesterday; // Return yesterday's date
	}

	public LocalDate generateFutureDate() {
		LocalDate today = LocalDate.now(); // Get today's date
		LocalDate futureDate = today.plusYears(2); // Add two years
		return futureDate; // Return the future date
	}

//	@Test(priority = 1)
	public void testRequestBodyWithRandomValuesCRMData() {
		// Generate random values for the request body
		String VIN_NO = "VIN_NO";
		String ICCID = "ICCID";
		String UIN_NO = generateRandomString(19);
		String DEVICE_IMEI = generateRandomNumber(15);
		String DEVICE_MAKE = "Accolade";
		String DEVICE_MODEL = "AEPL051401";
		String ENGINE_NO = generateRandomString(25);
		String REG_NUMBER = generateRandomString(15);
		String VEHICLE_OWNER_FIRST_NAME = generateRandomString(15);
		String VEHICLE_OWNER_MIDDLE_NAME = generateRandomString(15);
		String VEHICLE_OWNER_LAST_NAME = generateRandomString(15);
		String ADDRESS_LINE_1 = generateRandomString(50);
		String ADDRESS_LINE_2 = generateRandomString(50);
		String VEHICLE_OWNER_CITY = generateRandomString(15);
		String VEHICLE_OWNER_DISTRICT = generateRandomString(15);
		String VEHICLE_OWNER_STATE = generateRandomString(15);
		String VEHICLE_OWNER_COUNTRY = generateRandomString(15);
		String VEHICLE_OWNER_PINCODE = generateRandomNumber(6);
		String VEHICLE_OWNER_REGISTERED_MOBILE = generateRandomNumber(10);
		String POS_CODE = generateRandomString(10);
		String POA_DOC_NAME = generateRandomString(10);
		String POA_DOC_NO = generateRandomString(10);
		String POI_DOC_TYPE = generateRandomString(20);
		String POI_DOC_NO = generateRandomString(10);
		String RTO_OFFICE_CODE = generateRandomString(10);
		String RTO_STATE = generateRandomString(10);
		String PRIMARY_OPERATOR = "BSNL";
		String SECONDARY_OPERATOR = "BHA";
		String PRIMARY_MOBILE_NUMBER = generateRandomNumber(15);
		String SECONDARY_MOBILE_NUMBER = generateRandomNumber(15);
		String VEHICLE_MODEL = generateRandomString(15);
		String DEALER_CODE = generateRandomString(15);
		LocalDate COMMERCIAL_ACTIVATION_START_DATE = generateYesterdayDate();
		LocalDate COMMERCIAL_ACTIVATION_EXPIRY_DATE = generateFutureDate();
		int MFG_YEAR = generateRandomYear(4);
		String INVOICE_DATE = generateRandomDate();
		String INVOICE_NUMBER = generateRandomString(20);
		int CERTIFICATE_VALIDITY_DURATION_IN_YEAR = 2;
	}

//	@Test(priority = 2)
	void SaveCRMData() {
		apiTicketContext = aisTicketService.generateAndFetchTicket();
		if (apiTicketContext != null) {
			this.VIN_NO = apiTicketContext.getVinNo();
			this.ICCID = apiTicketContext.getIccid();
			this.Ticket_No = apiTicketContext.getTicketNo();
		}
	}

//	@Test(priority = 3)
	public void testGetTicketStatus() {
		SaveCRMData();
	}

	public String generateAisTicketFromApi() {
		SaveCRMData();
		return Ticket_No;
	}

	public String searchByTicketNo(String ticketNo) {
		WebElement search = actions.waitForVisibility(SearchBox);
		search.clear();
		search.sendKeys(ticketNo);
		search.sendKeys(Keys.ENTER);
		return search.getAttribute("value");
	}

	public void clickNavBar() {
		// Wait for the navigation bar links to be visible
		List<WebElement> navBarLinks = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(navBarLink));

		boolean isClicked = false;
		for (WebElement link : navBarLinks) {
			if (link.getText().equalsIgnoreCase("Device Utility")) {
				link.click();
//				System.out.println("Clicked On Element On Nav: " +link.getAccessibleName());
				isClicked = true;
//				break;
			}
		}
		if (!isClicked) {
			throw new RuntimeException("Failed to find and click on 'Device Utility' in the navigation bar.");
		}
	}

	public String clickDropDownOption() {
		// Click on the element 'My AIS140 Tickets Page' and return the current URL
		try {
			WebElement MyAis140Ticket = actions.waitForVisibility(MyAis140);
			Thread.sleep(2000);
			actions.clickElement(MyAis140Ticket);
			return driver.getCurrentUrl();
		} catch (Exception e) {
			logger.error("Error while clicking on My AIS140 Ticket option.", e);
			throw new RuntimeException("Failed to click on My AIS140 Ticket option", e);
		}
	}

	public WebElement ClickSearchBox() {
		try {
			// Wait for the navigation bar links to be visible
			WebElement search = actions.waitForVisibility(SearchBox);
			Thread.sleep(2000);
			actions.highlightElement(search, "solid yellow");
			return search;
		} catch (Exception e) {
			logger.error("Error while clicking on My AIS140 Ticket Search Box option.", e);
			throw new RuntimeException("Failed to click on My AIS140 Ticket Search Box option", e);
		}
	}

	public void ClickViewButton() throws InterruptedException {
		try {
			// Wait for the overlay to disappear
			wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
			
			  // Wait for the view button to be clickable
	        WebElement viewButtonElement = wait.until(ExpectedConditions.elementToBeClickable(viewButton));
			
			   // Scroll to the view button if it's not visible in the viewport
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", viewButtonElement);

	        // Click the view button
	        viewButtonElement.click();
			
			logger.info("Successfully clicked on the View button.");
			Thread.sleep(5000);

			// Optionally navigate back
//	        driver.navigate().back();
		} catch (Exception e) {
			logger.error("Error while clicking the View button.", e);
			throw new RuntimeException("Failed to click the View button.", e);
		}
	}

	public void ClickTicketInformation() {
		int tabIndex=1;
		  try {
		        // Wait for the overlay to disappear (if any)
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//		        wait.until(ExpectedConditions.invisibilityOfElementLocated(Arrow));		        
		        String originalTab = driver.getWindowHandle(); 
		        logger.info("Original tab handle stored: " + originalTab);
		        Set<String> allTabs = driver.getWindowHandles();
		        ArrayList<String> tabList = new ArrayList<>(allTabs);
		        if (tabIndex < 0 || tabIndex >= tabList.size()) {
		            logger.error("Invalid tab index: " + tabIndex + ". Total tabs open: " + tabList.size());
		            throw new RuntimeException("Invalid tab index: " + tabIndex);		    
		            }
		        String targetTab = tabList.get(tabIndex);
		        logger.info("Switching to tab with index: " + tabIndex + ", handle: " + targetTab);
		        driver.switchTo().window(targetTab);
		        // Wait for the particular element in the new tab to be clickable
		        WebElement elementToClickInNewTab = wait.until(ExpectedConditions.elementToBeClickable(Arrow));
		        elementToClickInNewTab.click();
		        logger.info("Successfully clicked the Ticket Information of the new tab.");
		    } catch (Exception e) {
		        logger.error("Error while clicking the new window/tab.", e);
		        throw new RuntimeException("Failed to clicking with the new window/tab.", e);
		    }
	}
	
	public void TicketNumber() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;

			// Wait for the element to be visible
			WebElement ticketNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(TicketNumber));

			// Scroll the element into view if necessary
//    	js.executeScript("arguments[0].scrollIntoView(true);", ticketNumberElement);

    	// Simulate a click using JavaScript if the element is not natively clickable
    	js.executeScript("arguments[0].click();", ticketNumberElement);

    	// Retrieve the "value" attribute
    	String inputValue = ticketNumberElement.getAttribute("value");
    
    	// Print Ticket Information
    	System.out.println("\u001B[1m\u001B[34mTicket Information:\u001B[0m");
    	System.out.println("\u001B[1m\u001B[35mTicket Number:\u001B[0m " + inputValue);

    	// Highlight the element for visual verification
    	js.executeScript("arguments[0].style.border='5px solid yellow';", ticketNumberElement);

		} catch (Exception e) {
			logger.error("Error while interacting with Ticket Information for Ticket Number.", e);
			throw new RuntimeException("Failed to read Ticket Information for Ticket Number.", e);
		}
	}
	
	public void TicketCretedTime() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;

			// Wait for the element to be visible
			WebElement ticketCreateTimeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(TicketCreate));
			js.executeScript("arguments[0].click();", ticketCreateTimeElement);
			String inputValue = ticketCreateTimeElement.getAttribute("value");   
			System.out.println("\u001B[1m\u001B[35mTicket Created Time & Date :\u001B[0m " + inputValue);    	
			js.executeScript("arguments[0].style.border='5px solid yellow';", ticketCreateTimeElement);
		  	} catch (Exception e) {
			logger.error("Error while interacting with Ticket Information for Ticket Created Time & Date.", e);
			throw new RuntimeException("Failed to read Ticket Information for Ticket Created Time & Date.", e);
		}
	}
	
	public void TicketAssignedTime() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement ticketAssignTimeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(TicketAssigned));
			js.executeScript("arguments[0].click();", ticketAssignTimeElement);
			String inputValue = ticketAssignTimeElement.getAttribute("value");   
   			System.out.println("\u001B[1m\u001B[35mTicket Assigned Time & Date :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", ticketAssignTimeElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Ticket Information for Ticket Assigned Time & Date.", e);
			throw new RuntimeException("Failed to read Ticket Information for Ticket Assigned Time & Date.", e);
		}
	}
	
	public void TicketCancelledCompletedTime() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement ticketCompleteCancelElement = wait.until(ExpectedConditions.visibilityOfElementLocated(TicketCompleted));
			js.executeScript("arguments[0].click();", ticketCompleteCancelElement);    	
			String inputValue = ticketCompleteCancelElement.getAttribute("value");   
			System.out.println("\u001B[1m\u001B[35mTicket Cancelled or Completed Time & Date :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", ticketCompleteCancelElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Ticket Information for Ticket Cancelled or Completed Time & Date.", e);
			throw new RuntimeException("Failed to read Ticket Information for Ticket Cancelled or Completed Time & Date.", e);
		}
	}
	
	public void TicketCertificateValidityDuration() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// Wait for the element to be visible
			WebElement ticketCertificateValidityDurationElement = wait.until(ExpectedConditions.visibilityOfElementLocated(TicketCertificate));
			js.executeScript("arguments[0].click();", ticketCertificateValidityDurationElement);
			String inputValue = ticketCertificateValidityDurationElement.getAttribute("value");   
			System.out.println("\u001B[1m\u001B[35mTicket Certificate Validity Duration in Year :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", ticketCertificateValidityDurationElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Ticket Information for Ticket Certificate Validity Duration in Year.", e);
			throw new RuntimeException("Failed to read Ticket Information for Ticket Certificate Validity Duration in Year.", e);
		}
	}
	
	public void TicketOverAllStatus() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement ticketOverAllStatusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(TicketStatus));
			js.executeScript("arguments[0].click();", ticketOverAllStatusElement);    	
			String inputValue = ticketOverAllStatusElement.getAttribute("value");      	
			System.out.println("\u001B[1m\u001B[35mTicket Over All Status :\u001B[0m " + inputValue); 
			js.executeScript("arguments[0].style.border='5px solid yellow';", ticketOverAllStatusElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Ticket Information for Ticket Over All Status.", e);
			throw new RuntimeException("Failed to read Ticket Information for Ticket Over All Status.", e);
		}
	}
	
	public void TicketOverAllRemark() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement ticketOverAllRemarkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(TicketRemark));
			js.executeScript("arguments[0].click();", ticketOverAllRemarkElement);    	
			String inputValue = ticketOverAllRemarkElement.getAttribute("value");       	
			System.out.println("\u001B[1m\u001B[35mTicket Over All Remark :\u001B[0m " + inputValue);    
			js.executeScript("arguments[0].style.border='5px solid yellow';", ticketOverAllRemarkElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Ticket Information for Ticket Over All Remark.", e);
			throw new RuntimeException("Failed to read Ticket Information for Ticket Over All Remark.", e);
		}
	}
	
	public void TicketGeneratedBy() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;			
			WebElement ticketGeneratedByElement = wait.until(ExpectedConditions.visibilityOfElementLocated(TicketGenrate));
			js.executeScript("arguments[0].click();", ticketGeneratedByElement);
			String inputValue = ticketGeneratedByElement.getAttribute("value");   
			System.out.println("\u001B[1m\u001B[35mTicket Generated By :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", ticketGeneratedByElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Ticket Information for Ticket Generated By.", e);
			throw new RuntimeException("Failed to read Ticket Information for Ticket Generated By.", e);
		}
	}

	public void TicketDescription() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;			
			WebElement ticketDescriptionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(TicketDesc));
			js.executeScript("arguments[0].click();", ticketDescriptionElement);
			String inputValue = ticketDescriptionElement.getAttribute("value");   
			System.out.println("\u001B[1m\u001B[35mTicket Description :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", ticketDescriptionElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Ticket Information for Ticket Description.", e);
			throw new RuntimeException("Failed to read Ticket Information for Ticket Description.", e);
		}
	}
	
	public void ClickDeviceInformation() {
		try {
			// Wait for the elements to be clickable
			List<WebElement> deviceList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(DeviceInfo));
			int indexToClick = 1; // Change this to the desired index
			// Check if the index is valid
			if (indexToClick >= 0 && indexToClick < deviceList.size()) {
				WebElement elementToClick = deviceList.get(indexToClick);
				// Wait until the specific element is clickable
				wait.until(ExpectedConditions.elementToBeClickable(elementToClick));
				// Click the element
				elementToClick.click();
				// Log or perform additional actions after clicking
				System.out.println("\u001B[1m\u001B[34mDevice Information Clicked \u001B[0m ");
				// Optionally, add a delay for visual confirmation (avoid in real tests)
				Thread.sleep(2000);
				} else {
					System.err.println("Invalid index: " + indexToClick + ". Total elements found: " + deviceList.size());
					throw new IllegalArgumentException("Index out of bounds for Device list.");
				}
			} catch (Exception e) {
				logger.error("Error while clicking with Device Information elements.", e);
				throw new RuntimeException("Failed to clicking with Device Information.", e);
			}
	}
	
	public String DeviceUINNumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			WebElement deviceUINElement = wait.until(ExpectedConditions.visibilityOfElementLocated(UINNumber));
			js.executeScript("arguments[0].click();", deviceUINElement);
			String inputValue = deviceUINElement.getAttribute("value");   
			
			// Print Ticket Information
	    	System.out.println("\u001B[1m\u001B[34mDevice Information:\u001B[0m");
			System.out.println("\u001B[1m\u001B[35mUIN Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", deviceUINElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Device Information for UIN Number.", e);
			throw new RuntimeException("Failed to read Device Information for UIN Number.", e);
		}
		return "inputValue";
	}
	
	public String DeviceIMEINumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement deviceIMEIElement = wait.until(ExpectedConditions.visibilityOfElementLocated(IMEINumber));
			js.executeScript("arguments[0].click();", deviceIMEIElement);
			String inputValue = deviceIMEIElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mIMEI Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", deviceIMEIElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Device Information for IMEI Number.", e);
			throw new RuntimeException("Failed to read Device Information for IMEI Number.", e);
		}
		return "inputValue";
	}
	
	public String DeviceICCIDNumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement deviceICCIDElement = wait.until(ExpectedConditions.visibilityOfElementLocated(ICCIDNumber));
			js.executeScript("arguments[0].click();", deviceICCIDElement);
			String inputValue = deviceICCIDElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mICCID Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", deviceICCIDElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Device Information for ICCID Number.", e);
			throw new RuntimeException("Failed to read Device Information for ICCID Number.", e);
		}
		return "inputValue";
	}
	
	public String DeviceModel() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement deviceModelElement = wait.until(ExpectedConditions.visibilityOfElementLocated(DeviceModel));
			js.executeScript("arguments[0].click();", deviceModelElement);
			String inputValue = deviceModelElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDevice Model :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", deviceModelElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Device Information for Device Model.", e);
			throw new RuntimeException("Failed to read Device Information for Device Model.", e);
		}
		return "inputValue";
	}
	
	public String DeviceMake() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement deviceMakeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(DeviceMake));
			js.executeScript("arguments[0].click();", deviceMakeElement);
			String inputValue = deviceMakeElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDevice Make :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", deviceMakeElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Device Information for Device Make.", e);
			throw new RuntimeException("Failed to read Device Information for Device Make.", e);
		}
		return "inputValue";
	}
	
	public String DevicePriOprName() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement devicePriOprNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(primaryOperatorName));
			js.executeScript("arguments[0].click();", devicePriOprNameElement);
			String inputValue = devicePriOprNameElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mPrimary Operator Name :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", devicePriOprNameElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Device Information for Primary Operator Name.", e);
			throw new RuntimeException("Failed to read Device Information for Primary Operator Name.", e);
		}
		return "inputValue";
	}
	
	public String DevicePriOprNumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement devicePriOprNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(primaryOperatorNumber));
			js.executeScript("arguments[0].click();", devicePriOprNumberElement);
			String inputValue = devicePriOprNumberElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mPrimary Operator Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", devicePriOprNumberElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Device Information for Primary Operator Number.", e);
			throw new RuntimeException("Failed to read Device Information for Primary Operator Number.", e);
		}
		return "inputValue";
	}
	
	public String DeviceSecOprName() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement deviceSecOprNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(secondaryOperatorName));
			js.executeScript("arguments[0].click();", deviceSecOprNameElement);
			String inputValue = deviceSecOprNameElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mSecondary Operator Name :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", deviceSecOprNameElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Device Information for Secondary Operator Name.", e);
			throw new RuntimeException("Failed to read Device Information for Secondary Operator Name.", e);
		}
		return "inputValue";
	}
	
	public String DeviceSecOprNumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement deviceSecOprNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(secondaryOperatorNumber));
			js.executeScript("arguments[0].click();", deviceSecOprNumberElement);
			String inputValue = deviceSecOprNumberElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mSecondary Operator Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", deviceSecOprNumberElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Device Information for Secondary Operator Number.", e);
			throw new RuntimeException("Failed to read Device Information for Secondary Operator Number.", e);
		}
		return "inputValue";
	}
	
	public void ClickVehicleOwnerInformation() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,400)");
			// Wait for the elements to be clickable
			List<WebElement> vehicleownerList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(DeviceInfo));
			int indexToClick = 2; // Change this to the desired index
			// Check if the index is valid
			if (indexToClick >= 0 && indexToClick < vehicleownerList.size()) {
				WebElement elementToClick = vehicleownerList.get(indexToClick);
				// Wait until the specific element is clickable
				wait.until(ExpectedConditions.elementToBeClickable(elementToClick));
				// Click the element
				elementToClick.click();
				// Log or perform additional actions after clicking
				System.out.println("\u001B[1m\u001B[34mVehicle Owner Information Clicked \u001B[0m ");
				// Optionally, add a delay for visual confirmation (avoid in real tests)
				Thread.sleep(2000);
				} else {
					System.err.println("Invalid index: " + indexToClick + ". Total elements found: " + vehicleownerList.size());
					throw new IllegalArgumentException("Index out of bounds for Device list.");
				}
			} catch (Exception e) {
				logger.error("Error while clicking with Vehicle Owner Information elements.", e);
				throw new RuntimeException("Failed to clicking with Vehicle Owner Information.", e);
			}
	}
	
	public String DeviceVehicleOwnerName() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleOwnerNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(vehicleOwnerName));
			js.executeScript("arguments[0].click();", VehicleOwnerNameElement);
			String inputValue = VehicleOwnerNameElement.getAttribute("value");   
			
			// Print Ticket Information
	    	System.out.println("\u001B[1m\u001B[34mVehicle Owner Information:\u001B[0m");
			System.out.println("\u001B[1m\u001B[35mVehicle Owner Name :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleOwnerNameElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Owner Information for Vehicle Owner Name.", e);
			throw new RuntimeException("Failed to read Vehicle Owner Information for Vehicle Owner Name.", e);
		}
		return "inputValue";
	}
	
	public String VehicleOwnerMobileNumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleOwnerMobileNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(vehicleOwnerMobileNumber));
			js.executeScript("arguments[0].click();", VehicleOwnerMobileNumberElement);
			String inputValue = VehicleOwnerMobileNumberElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Owner Mobile Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleOwnerMobileNumberElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Owner Information for Vehicle Owner Mobile Number.", e);
			throw new RuntimeException("Failed to read Vehicle Owner Information for Vehicle Owner Mobile Number.", e);
		}
		return "inputValue";
	}
	
	public String VehicleOwnerPOADocName() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleOwnerPOADOCNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(vehicleOwnerPOAdocname));
			js.executeScript("arguments[0].click();", VehicleOwnerPOADOCNameElement);
			String inputValue = VehicleOwnerPOADOCNameElement.getAttribute("value");   
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Owner POA DOC Name :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleOwnerPOADOCNameElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Owner Information for Vehicle Owner POA DOC Name.", e);
			throw new RuntimeException("Failed to read Vehicle Owner Information for Vehicle Owner POA DOC Name.", e);
		}
		return "inputValue";
	}
	
	public String VehicleOwnerPOADocNumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleOwnerPOADOCNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(vehicleOwnerPOAdocnumber));
			js.executeScript("arguments[0].click();", VehicleOwnerPOADOCNumberElement);
			String inputValue = VehicleOwnerPOADOCNumberElement.getAttribute("value");   
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Owner POA DOC Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleOwnerPOADOCNumberElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Owner Information for Vehicle Owner POA DOC Number.", e);
			throw new RuntimeException("Failed to read Vehicle Owner Information for Vehicle Owner POA DOC Number.", e);
		}
		return "inputValue";
	}
	
	public String VehicleOwnerPOIDocName() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleOwnerPOIDOCNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(vehicleOwnerPOIdocname));
			js.executeScript("arguments[0].click();", VehicleOwnerPOIDOCNameElement);
			String inputValue = VehicleOwnerPOIDOCNameElement.getAttribute("value");   
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Owner POI DOC Name :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleOwnerPOIDOCNameElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Owner Information for Vehicle Owner POI DOC Name.", e);
			throw new RuntimeException("Failed to read Vehicle Owner Information for Vehicle Owner POI DOC Name.", e);
		}
		return "inputValue";
	}
	
	public String VehicleOwnerPOIDocNumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleOwnerPOIDOCNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(vehicleOwnerPOIdocnumber));
			js.executeScript("arguments[0].click();", VehicleOwnerPOIDOCNumberElement);
			String inputValue = VehicleOwnerPOIDOCNumberElement.getAttribute("value");   
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Owner POI DOC Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleOwnerPOIDOCNumberElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Owner Information for Vehicle Owner POI DOC Number.", e);
			throw new RuntimeException("Failed to read Vehicle Owner Information for Vehicle Owner POI DOC Number.", e);
		}
		return "inputValue";
	}
	
	public String VehicleOwnerAddress() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleOwnerAddressElement = wait.until(ExpectedConditions.visibilityOfElementLocated(vehicleOwnerAddress));
			js.executeScript("arguments[0].click();", VehicleOwnerAddressElement);
			String inputValue = VehicleOwnerAddressElement.getAttribute("value");   
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Owner Address :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleOwnerAddressElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Owner Information for Vehicle Owner Address.", e);
			throw new RuntimeException("Failed to read Vehicle Owner Information for Vehicle Owner Address.", e);
		}
		return "inputValue";
	}
	
	public void ClickVehicleInformation() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			// Wait for the elements to be clickable
			List<WebElement> vehicleList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(DeviceInfo));
			int indexToClick = 3; // Change this to the desired index
			// Check if the index is valid
			if (indexToClick >= 0 && indexToClick < vehicleList.size()) {
				WebElement elementToClick = vehicleList.get(indexToClick);
				// Wait until the specific element is clickable
				wait.until(ExpectedConditions.elementToBeClickable(elementToClick));
				// Click the element
				elementToClick.click();
				// Log or perform additional actions after clicking
				System.out.println("\u001B[1m\u001B[34mVehicle Information Clicked \u001B[0m ");
				// Optionally, add a delay for visual confirmation (avoid in real tests)
				Thread.sleep(2000);
				} else {
					System.err.println("Invalid index: " + indexToClick + ". Total elements found: " + vehicleList.size());
					throw new IllegalArgumentException("Index out of bounds for Device list.");
				}
			} catch (Exception e) {
				logger.error("Error while clicking with Vehicle Information elements.", e);
				throw new RuntimeException("Failed to clicking with Vehicle Information.", e);
			}
	}
	
	public String VehicleModel() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleModelElement = wait.until(ExpectedConditions.visibilityOfElementLocated(vehiclemodel));
			js.executeScript("arguments[0].click();", VehicleModelElement);
			String inputValue = VehicleModelElement.getAttribute("value");   
			
			// Print Ticket Information
	    	System.out.println("\u001B[1m\u001B[34mVehicle Information:\u001B[0m");
			System.out.println("\u001B[1m\u001B[35mVehicle Model :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleModelElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Information for Vehicle Model.", e);
			throw new RuntimeException("Failed to read Vehicle Information for Vehicle Model.", e);
		}
		return "inputValue";
	}
	
	public String VehicleMake() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleMakeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(vehiclemake));
			js.executeScript("arguments[0].click();", VehicleMakeElement);
			String inputValue = VehicleMakeElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Model :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleMakeElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Information for Vehicle Make.", e);
			throw new RuntimeException("Failed to read Vehicle Information for Vehicle Make.", e);
		}
		return "inputValue";
	}
	
	public String VehicleManufactureYear() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleManufactureyearElement = wait.until(ExpectedConditions.visibilityOfElementLocated(manufacturingyear));
			js.executeScript("arguments[0].click();", VehicleManufactureyearElement);
			String inputValue = VehicleManufactureyearElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Manfacture Year :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleManufactureyearElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Information for Manfacture Year.", e);
			throw new RuntimeException("Failed to read Vehicle Information for Manfacture Year.", e);
		}
		return "inputValue";
	}
	
	public String VehicleChassisNumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleChassisNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(chassisnumber));
			js.executeScript("arguments[0].click();", VehicleChassisNumberElement);
			String inputValue = VehicleChassisNumberElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Chassis Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleChassisNumberElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Information for Chassis Number.", e);
			throw new RuntimeException("Failed to read Vehicle Information for Chassis Number.", e);
		}
		return "inputValue";
	}
	
	public String VehicleEngineNumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleEngineNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(enginenumber));
			js.executeScript("arguments[0].click();", VehicleEngineNumberElement);
			String inputValue = VehicleEngineNumberElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Engine Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleEngineNumberElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Information for Engine Number.", e);
			throw new RuntimeException("Failed to read Vehicle Information for Engine Number.", e);
		}
		return "inputValue";
	}
	
	public String VehicleRegistrationNumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleRegiNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(registrationnumber));
			js.executeScript("arguments[0].click();", VehicleRegiNumberElement);
			String inputValue = VehicleRegiNumberElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Registration Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleRegiNumberElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Information for Registration Number.", e);
			throw new RuntimeException("Failed to read Vehicle Information for Registration Number.", e);
		}
		return "inputValue";
	}
	
	public String VehicleInvoiceDate() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleInvoiceDateElement = wait.until(ExpectedConditions.visibilityOfElementLocated(invoicedate));
			js.executeScript("arguments[0].click();", VehicleInvoiceDateElement);
			String inputValue = VehicleInvoiceDateElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Invoice Date :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleInvoiceDateElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Information for Invoice Date.", e);
			throw new RuntimeException("Failed to read Vehicle Information for Invoice Date.", e);
		}
		return "inputValue";
	}
	
	public String VehicleInvoiceNumber() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleInvoiceNumberElement = wait.until(ExpectedConditions.visibilityOfElementLocated(invoicenumber));
			js.executeScript("arguments[0].click();", VehicleInvoiceNumberElement);
			String inputValue = VehicleInvoiceNumberElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle Invoice Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleInvoiceNumberElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Information for Invoice Number.", e);
			throw new RuntimeException("Failed to read Vehicle Information for Invoice Number.", e);
		}
		return "inputValue";
	}
	
	public String VehicleRTOState() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleRTOStateElement = wait.until(ExpectedConditions.visibilityOfElementLocated(rtostate));
			js.executeScript("arguments[0].click();", VehicleRTOStateElement);
			String inputValue = VehicleRTOStateElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle RTO State :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleRTOStateElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Information for RTO State.", e);
			throw new RuntimeException("Failed to read Vehicle Information for RTO State.", e);
		}
		return "inputValue";
	}
	
	public String VehicleRTOCode() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleRTOCodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(rtocode));
			js.executeScript("arguments[0].click();", VehicleRTOCodeElement);
			String inputValue = VehicleRTOCodeElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle RTO Code :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleRTOCodeElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Information for RTO Code.", e);
			throw new RuntimeException("Failed to read Vehicle Information for RTO Code.", e);
		}
		return "inputValue";
	}
	
	
	public WebElement VehicleIGNButton() {
		try {
			// Wait for the navigation bar links to be visible
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement reload = wait.until(ExpectedConditions.visibilityOfElementLocated(ignButton));
			Thread.sleep(2000);
				reload.click();
			js.executeScript("arguments[0].style.border='5px solid Yellow'");
			return reload;
		} catch (Exception e) {
			logger.error("Error while clicking on IGN Status Reload Button option.", e);
			throw new RuntimeException("Failed to click on IGN Status Reload Button option", e);
		}
	}
	
	
	public String VehicleIGNSatus() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement VehicleIGNStatusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(ignstatus));
			js.executeScript("arguments[0].click();", VehicleIGNStatusElement);
			String inputValue = VehicleIGNStatusElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mVehicle IGN Status :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", VehicleIGNStatusElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Vehicle Information for IGN Status.", e);
			throw new RuntimeException("Failed to read Vehicle Information for IGN Status.", e);
		}
		return "inputValue";
	}
	
	public void ClickDealerInformation() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			// Wait for the elements to be clickable
			List<WebElement> dealerList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(DeviceInfo));
			int indexToClick = 4; // Change this to the desired index
			// Check if the index is valid
			if (indexToClick >= 0 && indexToClick < dealerList.size()) {
				WebElement elementToClick = dealerList.get(indexToClick);
				// Wait until the specific element is clickable
				wait.until(ExpectedConditions.elementToBeClickable(elementToClick));
				// Click the element
				elementToClick.click();
				// Log or perform additional actions after clicking
				System.out.println("\u001B[1m\u001B[34mDealear Information Clicked \u001B[0m ");
				// Optionally, add a delay for visual confirmation (avoid in real tests)
				Thread.sleep(2000);
				} else {
					System.err.println("Invalid index: " + indexToClick + ". Total elements found: " + dealerList.size());
					throw new IllegalArgumentException("Index out of bounds for Device list.");
				}
			} catch (Exception e) {
				logger.error("Error while clicking with Dealear Information elements.", e);
				throw new RuntimeException("Failed to clicking with Dealear Information.", e);
			}
	}
	
	public String VehicleDealerCode() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement DealerCodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dealercode));
			js.executeScript("arguments[0].click();", DealerCodeElement);
			String inputValue = DealerCodeElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDealer Information :");
			System.out.println("\u001B[1m\u001B[35mDealer Code :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", DealerCodeElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Dealer Information for Dealer Code.", e);
			throw new RuntimeException("Failed to read Dealer Information for Dealer Code.", e);
		}
		return "inputValue";
	}

	public String VehicleDealerEmail() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement DealerEmailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dealeremail));
			js.executeScript("arguments[0].click();", DealerEmailElement);
			String inputValue = DealerEmailElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDealer Email :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", DealerEmailElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Dealer Information for Dealer Email.", e);
			throw new RuntimeException("Failed to read Dealer Information for Dealer Email.", e);
		}
		return "inputValue";
	}
	
	public String VehicleDealerCity() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement DealerCityElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dealercity));
			js.executeScript("arguments[0].click();", DealerCityElement);
			String inputValue = DealerCityElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDealer City :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", DealerCityElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Dealer Information for Dealer City.", e);
			throw new RuntimeException("Failed to read Dealer Information for Dealer City.", e);
		}
		return "inputValue";
	}
	
	public String VehicleDealerPhoneNo() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			WebElement DealerPhoneNoElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dealerphoneno));
			js.executeScript("arguments[0].click();", DealerPhoneNoElement);
			String inputValue = DealerPhoneNoElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDealer Phone Number :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", DealerPhoneNoElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Dealer Information for Dealer Phone Number.", e);
			throw new RuntimeException("Failed to read Dealer Information for Dealer Phone Number.", e);
		}
		return "inputValue";
	}
	
	public String VehicleDealerPOSName() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			WebElement DealerPOSNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(posname));
			js.executeScript("arguments[0].click();", DealerPOSNameElement);
			String inputValue = DealerPOSNameElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDealer POS Name :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", DealerPOSNameElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Dealer Information for Dealer POS Name.", e);
			throw new RuntimeException("Failed to read Dealer Information for Dealer POS Name.", e);
		}
		return "inputValue";
	}
	
	public String VehicleDealerPOSCode() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			WebElement DealerPOSCodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(poscode));
			js.executeScript("arguments[0].click();", DealerPOSCodeElement);
			String inputValue = DealerPOSCodeElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDealer POS Code :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", DealerPOSCodeElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Dealer Information for Dealer POS Code.", e);
			throw new RuntimeException("Failed to read Dealer Information for Dealer POS Code.", e);
		}
		return "inputValue";
	}
	
	public void ClickDeviceFOTAStatus() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			// Wait for the elements to be clickable
			List<WebElement>fotastatusList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(DeviceInfo));
			int indexToClick = 5; // Change this to the desired index
			// Check if the index is valid
			if (indexToClick >= 0 && indexToClick < fotastatusList.size()) {
				WebElement elementToClick = fotastatusList.get(indexToClick);
				// Wait until the specific element is clickable
				wait.until(ExpectedConditions.elementToBeClickable(elementToClick));
				// Click the element
				elementToClick.click();
				// Log or perform additional actions after clicking
				System.out.println("\u001B[1m\u001B[34mDevice FOTA Status Clicked \u001B[0m ");
				// Optionally, add a delay for visual confirmation (avoid in real tests)
				Thread.sleep(2000);
				} else {
					System.err.println("Invalid index: " + indexToClick + ". Total elements found: " + fotastatusList.size());
					throw new IllegalArgumentException("Index out of bounds for Device list.");
				}
			} catch (Exception e) {
				logger.error("Error while clicking with Device FOTA Status elements.", e);
				throw new RuntimeException("Failed to clicking with Device FOTA Status.", e);
			}
	}
	
	public String DeviceFOTABatchID() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement FoatBatchIDElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fotabatchid));
			js.executeScript("arguments[0].click();", FoatBatchIDElement);
			String inputValue = FoatBatchIDElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDevice FOTA Status Information :");
			System.out.println("\u001B[1m\u001B[35mFOTA Batch ID :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", FoatBatchIDElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Device FOTA Status for FOTA Batch ID.", e);
			throw new RuntimeException("Failed to read Device FOTA Status for FOTA Batch ID.", e);
		}
		return "inputValue";
	}
	
	public String VehicleFOTABatchID() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement FoatBatchIDElement = wait.until(ExpectedConditions.visibilityOfElementLocated(dealercode));
			js.executeScript("arguments[0].click();", FoatBatchIDElement);
			String inputValue = FoatBatchIDElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDevice FOTA Status Information :");
			System.out.println("\u001B[1m\u001B[35mFOTA Batch ID :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", FoatBatchIDElement);
			} catch (Exception e) {
			logger.error("Error while interacting with Device FOTA Status Information for FOTA Batch ID.", e);
			throw new RuntimeException("Failed to read Device FOTA Status Information for FOTA Batch ID.", e);
		}
		return "inputValue";
	}
	
	public String DeviceCurrentFWVer() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement CurrentFWVerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(currentfw));
			js.executeScript("arguments[0].click();", CurrentFWVerElement);
			String inputValue = CurrentFWVerElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mCurrent Firmware Version :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", CurrentFWVerElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for Current Firmware Version.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for Current Firmware Version.", e);
		}
		return "inputValue";
	}
	
	public String DeviceAssignedFWVer() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement AssignedFWVerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(assinedfw));
			js.executeScript("arguments[0].click();", AssignedFWVerElement);
			String inputValue = AssignedFWVerElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mAssigned Firmware Version :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", AssignedFWVerElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for Assigned Firmware Version.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for Assigned Firmware Version.", e);
		}
		return "inputValue";
	}
	
	public String DeviceFOTAStatus() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement FOTAStatusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fotastatus));
			js.executeScript("arguments[0].click();", FOTAStatusElement);
			String inputValue = FOTAStatusElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mFOTA Status :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", FOTAStatusElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for FOTA Status.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for FOTA Status.", e);
		}
		return "inputValue";
	}
	
	public String DeviceFOTAProgress() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement FOTAProgressElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fotaprogress));
			js.executeScript("arguments[0].click();", FOTAProgressElement);
			String inputValue = FOTAProgressElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mFOTA Progress in % :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", FOTAProgressElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for FOTA Progress in %.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for FOTA Progress in %.", e);
		}
		return "inputValue";
	}
	
	public String DeviceOTAPriIP() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement OTAPriIPElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fotapriip));
			js.executeScript("arguments[0].click();", OTAPriIPElement);
			String inputValue = OTAPriIPElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mOTA Primary IP :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", OTAPriIPElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for OTA Primary IP.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for OTA Primary IP.", e);
		}
		return "inputValue";
	}
	
	public String DeviceOTAPriStatus() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement OTAPriIPStatusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fotapriipstatus));
			js.executeScript("arguments[0].click();", OTAPriIPStatusElement);
			String inputValue = OTAPriIPStatusElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mOTA Primary IP Status :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", OTAPriIPStatusElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for OTA Primary IP Status.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for OTA Primary IP Status.", e);
		}
		return "inputValue";
	}
	
	public String DeviceOTASecIP() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement OTASecIPElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fotasecip));
			js.executeScript("arguments[0].click();", OTASecIPElement);
			String inputValue = OTASecIPElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mOTA Secondary IP :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", OTASecIPElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for OTA Secondary IP.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for OTA Secondary IP.", e);
		}
		return "inputValue";
	}
	
	public String DeviceOTASecStatus() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement OTASecIPStatusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(fotasecipstatus));
			js.executeScript("arguments[0].click();", OTASecIPStatusElement);
			String inputValue = OTASecIPStatusElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mOTA Secondary IP Status :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", OTASecIPStatusElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for OTA Secondary IP Status.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for OTA Secondary IP Status.", e);
		}
		return "inputValue";
	}
	
	public String DeviceStateEnOTAStatus() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement OTAStateEnStatusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(stateenableota));
			js.executeScript("arguments[0].click();", OTAStateEnStatusElement);
			String inputValue = OTAStateEnStatusElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDevice Enable OTA Status :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", OTAStateEnStatusElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for Device Enable OTA Status.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for Device Enable OTA Status.", e);
		}
		return "inputValue";
	}
	
	public String DeviceStage2Restriction() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement OTAStateEnStatusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(Reason2skipstage));
			js.executeScript("arguments[0].click();", OTAStateEnStatusElement);
			
			
			WebElement stage2SkipElement = wait.until(ExpectedConditions.visibilityOfElementLocated(selectskipstage2));
			js.executeScript("arguments[0].click();", stage2SkipElement);
			String inputValue = stage2SkipElement.getAttribute("value");			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mStage 2 Restriction Remove :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", OTAStateEnStatusElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for Stage 2 Restriction Remove.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for Stage 2 Restriction Remove.", e);
		}
		return "inputValue";
	}
	
	public String DeviceFOTASkipRemark() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement FOTASkipRemarkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(Remark2skipstage));
			FOTASkipRemarkElement.sendKeys("Skip FOTA & Remove Stage 2 Restriction");
			js.executeScript("arguments[0].click();", FOTASkipRemarkElement);
			String inputValue = FOTASkipRemarkElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDevice FOTA Skip Remark :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", FOTASkipRemarkElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for Device FOTA Skip Remark.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for Device FOTA Skip Remark.", e);
		}
		return "inputValue";
	}
	
	public String DeviceFOTASkipBy() { 
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement FOTASkipByElement = wait.until(ExpectedConditions.visibilityOfElementLocated(skippedby));
			js.executeScript("arguments[0].click();", FOTASkipByElement);
			String inputValue = FOTASkipByElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mDevice FOTA Skip By User Name :\u001B[0m " + inputValue);
			js.executeScript("arguments[0].style.border='5px solid yellow';", FOTASkipByElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for Device FOTA Skip By User Name.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for Device FOTA Skip By User Name.", e);
		}
		return "inputValue";
	}
	
	public String RemoveStage2RestrictionButton() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;	
//			js.executeScript("window.scrollBy(0,100)");
			WebElement Removestage2resElement = wait.until(ExpectedConditions.visibilityOfElementLocated(removestage2restriction));
			js.executeScript("arguments[0].click();", Removestage2resElement);
//			String inputValue = Removestage2resElement.getAttribute("value");   
			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mRemove Stage 2 Restriction Button Clicked :\u001B[0m " );
			js.executeScript("arguments[0].style.border='5px solid yellow';", Removestage2resElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Device FOTA Status Information for Remove Stage 2 Restriction Button option.", e);
			throw new RuntimeException("Failed to read  Device FOTA Status Information for Remove Stage 2 Restriction Button option.", e);
		}
		return "inputValue";
	}
	
	public String DeviceIgnOnOff() { 
		try {
			Thread.sleep(4000);
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,100)");
			WebElement IgnOnOffElement = wait.until(ExpectedConditions.visibilityOfElementLocated(VehicleIgnitionOn));
			js.executeScript("arguments[0].click();", IgnOnOffElement);
			wait.until(ExpectedConditions.elementToBeClickable(IgnOnOffElement));
			System.out.println("\u001B[1m\u001B[35mStage 1 of 1st substage dropdown is Clicked :\u001B[0m " );
			
			Thread.sleep(2000);
			// Locate the dropdown element
	        WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(VehicleIgnitionOnOption));
	        js.executeScript("arguments[0].click();", dropdownElement);

	        WebElement Substage1RemarkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(Remarkforsubstage1));
	        js.executeScript("arguments[0].click();", Substage1RemarkElement);
	        Substage1RemarkElement.sendKeys("Vehicle Ignition On continue to next stage");
			
	        WebElement OverAllTicketStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(overallticketstatus));
	        js.executeScript("arguments[0].click();", OverAllTicketStatus);
	        
	        WebElement inprogressTicketStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(inprogressstatus));
	        js.executeScript("arguments[0].click();", inprogressTicketStatus);
	     
	        WebElement ClickUpdatebtn = wait.until(ExpectedConditions.visibilityOfElementLocated(updatebutton));
	        js.executeScript("arguments[0].click();", ClickUpdatebtn);
	        
			String inputValue = IgnOnOffElement.getAttribute("value");			
			// Print Ticket Information
			System.out.println("\u001B[1m\u001B[35mStage 1 of 1st substage is Completed :\u001B[0m ");
			js.executeScript("arguments[0].style.border='5px solid yellow';", IgnOnOffElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Stage 1 of 1st substage dropdown is Clicked.", e);
			throw new RuntimeException("Failed to read  Stage 1 of 1st substage dropdown is Clicked.", e);
		}
		return "inputValue";
	}
	
	public String GSMNWAvailable() { 
		try {
			Thread.sleep(4000);
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,-100)");
			WebElement GSMNWElement = wait.until(ExpectedConditions.visibilityOfElementLocated(gsmnw));
			js.executeScript("arguments[0].click();", GSMNWElement);
			wait.until(ExpectedConditions.elementToBeClickable(GSMNWElement));
			System.out.println("\u001B[1m\u001B[35mStage 1 of 2nd substage dropdown is Clicked :\u001B[0m " );
			
			Thread.sleep(2000);
			// Locate the dropdown element
	        WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(gsmnwoption));
	        js.executeScript("arguments[0].click();", dropdownElement);

	        WebElement Substage2RemarkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(Remarkforsubstage2));
	        js.executeScript("arguments[0].click();", Substage2RemarkElement);
	        Substage2RemarkElement.sendKeys("GSM Network Available continue to next stage");
	     
	        WebElement ClickUpdatebtn = wait.until(ExpectedConditions.visibilityOfElementLocated(updatebutton));
	        js.executeScript("arguments[0].click();", ClickUpdatebtn);
	        
			String inputValue = GSMNWElement.getDomAttribute("value");			
			// Print Ticket Information
//			System.out.println("\u001B[1m\u001B[35mStage 2 Restriction Remove :\u001B[0m ");
			js.executeScript("arguments[0].style.border='5px solid yellow';", GSMNWElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Stage 1 of 2nd substage dropdown is Clicked.", e);
			throw new RuntimeException("Failed to read  Stage 1 of 2nd substage dropdown is Clicked.", e);
		}
		return "inputValue";
	}
	
	public String GPSFixAvailable() { 
		try {
			Thread.sleep(4000);
			JavascriptExecutor js = (JavascriptExecutor) driver;	
			js.executeScript("window.scrollBy(0,-100)");
			WebElement GSMNWElement = wait.until(ExpectedConditions.visibilityOfElementLocated(gpsfix));
			js.executeScript("arguments[0].click();", GSMNWElement);
			wait.until(ExpectedConditions.elementToBeClickable(GSMNWElement));
			System.out.println("\u001B[1m\u001B[35mStage 1 of 2nd substage dropdown is Clicked :\u001B[0m " );
			
			Thread.sleep(2000);
			// Locate the dropdown element
	        WebElement dropdownElement = wait.until(ExpectedConditions.visibilityOfElementLocated(gpsfixoption));
	        js.executeScript("arguments[0].click();", dropdownElement);

	        WebElement Substage2RemarkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(Remarkforsubstage3));
	        js.executeScript("arguments[0].click();", Substage2RemarkElement);
	        Substage2RemarkElement.sendKeys("GPS Fix Available continue to next stage");
	     
	        WebElement ClickUpdatebtn = wait.until(ExpectedConditions.visibilityOfElementLocated(updatebutton));
	        js.executeScript("arguments[0].click();", ClickUpdatebtn);
	        
			String inputValue = GSMNWElement.getDomAttribute("value");			
			// Print Ticket Information
//			System.out.println("\u001B[1m\u001B[35mStage 2 Restriction Remove :\u001B[0m ");
			js.executeScript("arguments[0].style.border='5px solid yellow';", GSMNWElement);
			} catch (Exception e) {
			logger.error("Error while interacting with  Stage 1 of 3rd substage dropdown is Clicked.", e);
			throw new RuntimeException("Failed to read  Stage 1 of 3rd substage dropdown is Clicked.", e);
		}
		return "inputValue";
	}
	
}
