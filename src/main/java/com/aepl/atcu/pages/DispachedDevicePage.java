package com.aepl.atcu.pages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.atcu.util.PageActionsUtil;
import com.aepl.atcu.util.MouseActions;

public class DispachedDevicePage {

	// Global variables
	private final WebDriver driver;
	private final WebDriverWait wait;
	private PageActionsUtil commonMethod;
	private MouseActions mouse;
	private final Logger logger = LogManager.getLogger(DispachedDevicePage.class);

	private final By dropDown = By.xpath("//span[@class='headers_custom color_3D5772']");
	private final By DispatchedDevice = By.xpath("//a[@routerlink='dispatch-device-page']");
	private final By AddDispatchedDevice = By.xpath("//button[contains(.,'Add Dispatched Device')]");
	private final By downloadSampleButton = By.xpath("//button[contains(.,'Download Sample')]");

	public DispachedDevicePage(WebDriver driver) {
		this.driver = driver;
		this.mouse = new MouseActions(driver);
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

	public String clickDispatchedDevice() {
		// Click on the element 'Device Model' and return the current URL
		try {
			WebElement DispatchedDeviceLink = wait
					.until(ExpectedConditions.visibilityOfElementLocated(DispatchedDevice));
			DispatchedDeviceLink.click();
			return driver.getCurrentUrl();
		} catch (Exception e) {
			logger.error("Error while clicking on Dispatched Device option.", e);
			throw new RuntimeException("Failed to click on Dispatched Device option", e);
		}
	}

	public String clickAddDispatchedDevice() {
		// Click on the element 'Add Device Model' and return the current URL
		try {
			WebElement AddDispatchedDeviceBtn = wait
					.until(ExpectedConditions.visibilityOfElementLocated(AddDispatchedDevice));
			AddDispatchedDeviceBtn.click();
			return driver.getCurrentUrl();
		} catch (Exception e) {
			logger.error("Error while clicking on Add Dispatched Device button.", e);
			throw new RuntimeException("Failed to click on Add Dispatched Device button", e);
		}
	}

	public void clickdownloadSample() {
		try {
			WebElement downloadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(downloadSampleButton));

			commonMethod.checkReportDownloadForAllbuttons(downloadButton);
			downloadButton.click();
			wait.until(ExpectedConditions.invisibilityOf(downloadButton));
		} catch (Exception e) {
			logger.error("Error while downloading the sample report.", e);
			throw new RuntimeException("Failed to download the sample report", e);
		}
	}

	public void uploadFile(String dir, String filePrefix) throws AWTException {
		try {
			WebElement chooseFile = driver.findElement(By.id("txtFileUpload"));
			mouse.moveToElement(chooseFile);

			mouse.clickElement(chooseFile);

			Thread.sleep(3000);

			StringSelection selection = new StringSelection(dir + File.separator + filePrefix);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

			Robot robot = new Robot();
			robot.delay(2000);

			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);

		} catch (Exception e) {

		}
	}
}
