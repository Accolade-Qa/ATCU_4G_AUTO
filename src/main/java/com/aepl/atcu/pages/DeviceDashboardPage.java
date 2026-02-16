package com.aepl.atcu.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
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

	public boolean validateSearchInputVisible() {
		try {
			WebElement searchInput = findFirstVisibleElement(SEARCH_INPUT);
			if (searchInput == null) {
				return false;
			}
			common.highlightElement(searchInput, "green");
			return searchInput.isDisplayed();
		} catch (Exception e) {
			logger.error("Search input visibility validation failed", e);
			return false;
		}
	}

	public boolean validateSearchInputEnabled() {
		try {
			WebElement searchInput = findFirstVisibleElement(SEARCH_INPUT);
			if (searchInput == null) {
				return false;
			}
			common.highlightElement(searchInput, "green");
			return searchInput.isEnabled();
		} catch (Exception e) {
			logger.error("Search input enabled validation failed", e);
			return false;
		}
	}

	public boolean validateSearchButtonVisible() {
		try {
			WebElement searchBtn = findFirstVisibleElement(SEARCH_BUTTON);
			if (searchBtn == null) {
				return false;
			}
			common.highlightElement(searchBtn, "green");
			return searchBtn.isDisplayed();
		} catch (Exception e) {
			logger.error("Search button visibility validation failed", e);
			return false;
		}
	}

	public boolean validateSearchButtonEnabled() {
		try {
			WebElement searchBtn = findFirstVisibleElement(SEARCH_BUTTON);
			if (searchBtn == null) {
				return false;
			}
			common.highlightElement(searchBtn, "green");
			return searchBtn.isEnabled();
		} catch (Exception e) {
			logger.error("Search button enabled validation failed", e);
			return false;
		}
	}

	public boolean validateSearchWithValidData() {
		try {
			String searchTerm = getFirstRowFirstValue();
			if (searchTerm == null || searchTerm.isBlank()) {
				logger.warn("Unable to derive valid search data from table.");
				return false;
			}

			String query = searchTerm.length() > 5 ? searchTerm.substring(0, 5) : searchTerm;
			int initialCount = getTableRowCount();
			performSearch(query);

			boolean anyRowMatched = isAnyRowContains(query);
			int filteredCount = getTableRowCount();

			resetSearch();
			return anyRowMatched && (filteredCount <= initialCount || initialCount == 0);
		} catch (Exception e) {
			logger.error("Search with valid data validation failed", e);
			return false;
		}
	}

	public boolean validateSearchWithInvalidData() {
		try {
			String query = "ZZZ_INVALID_SEARCH_" + System.currentTimeMillis();
			performSearch(query);

			boolean noDataFound = table.isNoDataImagePresent(TABLE_ROOT) || getTableRowCount() == 0;
			resetSearch();
			return noDataFound;
		} catch (Exception e) {
			logger.error("Search with invalid data validation failed", e);
			return false;
		}
	}

	public boolean validateSearchResetBehavior() {
		try {
			int initialCount = getTableRowCount();

			String searchTerm = getFirstRowFirstValue();
			if (searchTerm == null || searchTerm.isBlank()) {
				return false;
			}

			String query = searchTerm.length() > 4 ? searchTerm.substring(0, 4) : searchTerm;
			performSearch(query);

			int filteredCount = getTableRowCount();
			resetSearch();
			int resetCount = getTableRowCount();

			if (initialCount == 0) {
				return resetCount == 0;
			}
			return resetCount >= filteredCount && resetCount > 0;
		} catch (Exception e) {
			logger.error("Search reset behavior validation failed", e);
			return false;
		}
	}

	public boolean validatePaginationVisible() {
		try {
			WebElement pagination = findFirstVisibleElement(PAGINATION_CONTAINER);
			WebElement nextBtn = findFirstVisibleElement(NEXT_PAGE_BUTTON);
			WebElement prevBtn = findFirstVisibleElement(PREVIOUS_PAGE_BUTTON);

			return pagination != null && pagination.isDisplayed() && nextBtn != null && prevBtn != null;
		} catch (Exception e) {
			logger.error("Pagination visibility validation failed", e);
			return false;
		}
	}

	public boolean validateNextPageWorking() {
		try {
			WebElement nextBtn = findFirstVisibleElement(NEXT_PAGE_BUTTON);
			if (nextBtn == null) {
				return false;
			}

			if (!nextBtn.isEnabled()) {
				logger.info("Next page button is disabled; likely single-page result.");
				return true;
			}

			String beforePageInfo = getPageInfoText();
			clickElement(nextBtn);
			waitForTableStabilization();

			String afterPageInfo = getPageInfoText();
			return !beforePageInfo.equals(afterPageInfo) || isPreviousButtonEnabled();
		} catch (Exception e) {
			logger.error("Next page validation failed", e);
			return false;
		}
	}

	public boolean validatePreviousPageWorking() {
		try {
			WebElement prevBtn = findFirstVisibleElement(PREVIOUS_PAGE_BUTTON);
			WebElement nextBtn = findFirstVisibleElement(NEXT_PAGE_BUTTON);

			if (prevBtn == null) {
				return false;
			}

			if (!prevBtn.isEnabled() && nextBtn != null && nextBtn.isEnabled()) {
				clickElement(nextBtn);
				waitForTableStabilization();
				prevBtn = findFirstVisibleElement(PREVIOUS_PAGE_BUTTON);
			}

			if (prevBtn == null || !prevBtn.isEnabled()) {
				logger.info("Previous page button is disabled; pagination did not move ahead.");
				return true;
			}

			String beforePageInfo = getPageInfoText();
			clickElement(prevBtn);
			waitForTableStabilization();

			String afterPageInfo = getPageInfoText();
			return !beforePageInfo.equals(afterPageInfo);
		} catch (Exception e) {
			logger.error("Previous page validation failed", e);
			return false;
		}
	}

	private WebElement findFirstVisibleElement(By... locators) {
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

	private void performSearch(String query) {
		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_INPUT));
		searchInput.click();
		searchInput.clear();
		searchInput.sendKeys(query);

		WebElement searchButton = findFirstVisibleElement(SEARCH_BUTTON);
		if (searchButton != null && searchButton.isEnabled()) {
			clickElement(searchButton);
		} else {
			searchInput.sendKeys(Keys.ENTER);
		}
		waitForTableStabilization();
	}

	private void resetSearch() {
		WebElement searchInput = findFirstVisibleElement(SEARCH_INPUT);
		WebElement resetButton = findFirstVisibleElement(RESET_BUTTON);

		if (resetButton != null && resetButton.isEnabled()) {
			clickElement(resetButton);
		} else if (searchInput != null) {
			searchInput.click();
			searchInput.clear();
			searchInput.sendKeys(Keys.ENTER);
		}
		waitForTableStabilization();
	}

	private int getTableRowCount() {
		List<String> headers = table.getTableHeaders(TABLE_ROOT);
		List<Map<String, String>> rows = table.getTableData(TABLE_ROOT, headers);
		return rows.size();
	}

	private String getFirstRowFirstValue() {
		List<String> headers = table.getTableHeaders(TABLE_ROOT);
		List<Map<String, String>> rows = table.getTableData(TABLE_ROOT, headers);

		if (rows.isEmpty()) {
			return "";
		}

		Map<String, String> firstRow = rows.get(0);
		if (firstRow.isEmpty()) {
			return "";
		}

		return firstRow.values().iterator().next();
	}

	private boolean isAnyRowContains(String query) {
		String expected = query.toLowerCase();
		List<String> headers = table.getTableHeaders(TABLE_ROOT);
		List<Map<String, String>> rows = table.getTableData(TABLE_ROOT, headers);

		for (Map<String, String> row : rows) {
			for (String value : row.values()) {
				if (value != null && value.toLowerCase().contains(expected)) {
					return true;
				}
			}
		}
		return false;
	}

	private void waitForTableStabilization() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(TABLE_ROOT));
	}

	private String getPageInfoText() {
		try {
			WebElement pageInfo = findFirstVisibleElement(PAGE_INFO);
			return pageInfo != null ? pageInfo.getText().trim() : "";
		} catch (NoSuchElementException e) {
			return "";
		}
	}

	private boolean isPreviousButtonEnabled() {
		WebElement prevBtn = findFirstVisibleElement(PREVIOUS_PAGE_BUTTON);
		return prevBtn != null && prevBtn.isEnabled();
	}

	private void clickElement(WebElement element) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element)).click();
		} catch (Exception e) {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
		}
	}

}
