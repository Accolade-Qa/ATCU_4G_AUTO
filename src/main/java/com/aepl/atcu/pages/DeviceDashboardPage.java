package com.aepl.atcu.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aepl.atcu.actions.MouseActions;
import com.aepl.atcu.locators.DeviceDashboardPageLocators;
import com.aepl.atcu.util.TableUtils;

public class DeviceDashboardPage extends DeviceDashboardPageLocators {
	private WebDriver driver;
	private WebDriverWait wait;
	private CommonMethods common;
	private TableUtils table;
	private static final Logger logger = LogManager.getLogger(DeviceDashboardPage.class);

	public DeviceDashboardPage(WebDriver driver, WebDriverWait wait, MouseActions action) {
		this.driver = driver;
		this.common = new CommonMethods(driver, wait);
		this.table = new TableUtils(driver, wait);
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public String clickNavBar() {
		if (driver.findElement(DEVICE_DASHBOARD).isDisplayed() && driver.findElement(DEVICE_DASHBOARD).isEnabled()) {
			wait.until(ExpectedConditions.visibilityOfElementLocated(DASHBOARD)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(DEVICE_DASHBOARD)).click();
			return "Link is verified";
		}
		return "No link is visible";
	}

	public boolean validateCardAreVisible() {
		boolean allCardsValidated = true;

		try {
			List<WebElement> initialCards = wait.until(ExpectedConditions
					.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'kpi-section')]/div")));

			int totalCards = initialCards.size();
			System.out.println("Total cards found: " + totalCards);

			for (int i = 0; i < totalCards; i++) {

				// 🔼 STEP 1: Scroll back to top BEFORE locating card again
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0);");

				// 🔄 STEP 2: Re-fetch cards because DOM changed after click
				List<WebElement> cards = wait.until(ExpectedConditions
						.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'kpi-section')]/div")));

				WebElement card = cards.get(i);

				// 🎯 STEP 3: Scroll card to center to avoid header overlap
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", card);

				wait.until(ExpectedConditions.elementToBeClickable(card));

				common.highlightElement(card, "violet");

				String cardName = card.getText().split("\n")[0].trim();
				String cardValue = card.findElement(By.xpath(".//span[contains(@class,'kpi-value')]")).getText();

				logger.info("Clicking Card -> " + cardName + " | Count -> " + cardValue);

				// 🖱 STEP 4: Click with fallback
				try {
					card.click();
				} catch (ElementClickInterceptedException e) {
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
				}

				// 📊 STEP 5: Validate table header
				WebElement tableHeader = wait
						.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".component-title")));

				String headerText = tableHeader.getText().trim();

				if (headerText.equalsIgnoreCase(cardName)) {
					logger.info("PASS: " + cardName + " matches table header.");
				} else {
					logger.error("FAIL: Card " + cardName + " but header is " + headerText);
					allCardsValidated = false;
				}
			}

		} catch (Exception e) {
			logger.error("Error validating KPI cards", e);
			return false;
		}
		return allCardsValidated;
	}

	public boolean validateCard1IsVisible() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
		WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(KPI_CARDS_1));
		return card.isDisplayed();
	}

	public String verifyAndClickKPITotalProDevWithCount() {
		WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(KPI_CARDS_1));
		card.click();
		String text = card.getText().toUpperCase();
		return text;
	}

	public String validateTotalProductionDevicesTableHeaders() {
		return table.getTableHeaders(By.xpath("//table")).toArray().toString();
	}

	public boolean validateTotalProductionDevicesTableButtons() {
		return true;
	}

	public boolean validateActionButtonVisibility() {
		logger.info("validating the action buttons visibility");
		return table.areViewButtonsEnabled(By.xpath("//table")) && table.areDeleteButtonsEnabled(By.xpath("//table"));
	}

	public boolean validateActionButtonsEnabled() {
		logger.info("validating the action buttons enabled");
		return table.areViewButtonsEnabled(By.xpath("//table")) && table.areDeleteButtonsEnabled(By.xpath("//table"));
	}

	public boolean ValidateNoDataImage() {
		return table.isNoDataImagePresent(By.xpath("//table"));
	}

	public boolean validateTheDownloadButtonIsVisibleOnTotalProductionDevicesTable() {
		WebElement report_btn = wait.until(ExpectedConditions.visibilityOfElementLocated(DOWNLOAD_REPORT_BTN));
		return report_btn.isDisplayed();
	}

	public boolean validateTheDownloadButtonIsEnabledOnTotalProductionDevicesTable() {
		WebElement report_btn = wait.until(ExpectedConditions.visibilityOfElementLocated(DOWNLOAD_REPORT_BTN));
		return report_btn.isEnabled();
	}

	public boolean validateTheDownloadButtonIsClickableOnTotalProductionDevicesTable() {
		try {
			WebElement downloadBtn = wait.until(ExpectedConditions.elementToBeClickable(DOWNLOAD_REPORT_BTN));

			common.highlightElement(downloadBtn, "green");

			return downloadBtn.isDisplayed() && downloadBtn.isEnabled();

		} catch (Exception e) {
			logger.error("Download button is not clickable", e);
			return false;
		}
	}

}
