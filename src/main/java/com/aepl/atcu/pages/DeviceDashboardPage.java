package com.aepl.atcu.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.aepl.atcu.locators.DeviceDashboardPageLocators.*;
import com.aepl.atcu.util.MouseActions;
import com.aepl.atcu.util.PageActionsUtil;
import com.aepl.atcu.util.TableUtils;

public class DeviceDashboardPage {
	private final WebDriver driver;
	private final WebDriverWait wait;
	private final PageActionsUtil common;
	private final TableUtils table;
	private static final Logger logger = LogManager.getLogger(DeviceDashboardPage.class);

	public DeviceDashboardPage(WebDriver driver, WebDriverWait wait, MouseActions action) {
		this.driver = driver;
		this.wait = (wait != null) ? wait : new WebDriverWait(driver, Duration.ofSeconds(10));
		this.common = new PageActionsUtil(driver, this.wait);
		this.table = new TableUtils(driver, this.wait);
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
			logger.info("Total cards found: " + totalCards);

			for (int i = 0; i < totalCards; i++) {

				// ðŸ”¼ STEP 1: Scroll back to top BEFORE locating card again
				((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0);");

				// ðŸ”„ STEP 2: Re-fetch cards because DOM changed after click
				List<WebElement> cards = wait.until(ExpectedConditions
						.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'kpi-section')]/div")));

				WebElement card = cards.get(i);

				// ðŸŽ¯ STEP 3: Scroll card to center to avoid header overlap
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", card);

				wait.until(ExpectedConditions.elementToBeClickable(card));

				common.highlightElement(card, "violet");

				String cardName = card.getText().split("\n")[0].trim();
				String cardValue = card.findElement(By.xpath(".//span[contains(@class,'kpi-value')]")).getText();

				logger.info("Clicking Card -> " + cardName + " | Count -> " + cardValue);

				// ðŸ–± STEP 4: Click with fallback
				try {
					card.click();
				} catch (ElementClickInterceptedException e) {
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
				}

				// ðŸ“Š STEP 5: Validate table header
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
		List<String> headers = table.getTableHeaders(TABLE_ROOT);
		return String.join(", ", headers);
	}

	public boolean validateTotalProductionDevicesTableButtons() {
		return table.areViewButtonsEnabled(TABLE_ROOT) && table.areDeleteButtonsEnabled(TABLE_ROOT);
	}

	public boolean validateActionButtonVisibility() {
		logger.info("validating the action buttons visibility");
		return table.areViewButtonsEnabled(TABLE_ROOT) && table.areDeleteButtonsEnabled(TABLE_ROOT);
	}

	public boolean validateActionButtonsEnabled() {
		logger.info("validating the action buttons enabled");
		return table.areViewButtonsEnabled(TABLE_ROOT) && table.areDeleteButtonsEnabled(TABLE_ROOT);
	}

	public boolean ValidateNoDataImage() {
		return table.isNoDataImagePresent(By.xpath("//table"));
	}

	public boolean validateTheDownloadButtonIsVisibleOnTotalProductionDevicesTable() {
		return common.isElementVisible(DOWNLOAD_REPORT_BTN);
	}

	public boolean validateTheDownloadButtonIsEnabledOnTotalProductionDevicesTable() {
		return common.isElementEnabled(DOWNLOAD_REPORT_BTN);
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
			return common.isElementVisible(SEARCH_INPUT);
		} catch (Exception e) {
			logger.error("Search input visibility validation failed", e);
			return false;
		}
	}

	public boolean validateSearchInputEnabled() {
		try {
			return common.isElementEnabled(SEARCH_INPUT);
		} catch (Exception e) {
			logger.error("Search input enabled validation failed", e);
			return false;
		}
	}

	public boolean validateSearchButtonVisible() {
		try {
			return common.isElementVisible(SEARCH_BUTTON);
		} catch (Exception e) {
			logger.error("Search button visibility validation failed", e);
			return false;
		}
	}

	public boolean validateSearchButtonEnabled() {
		try {
			return common.isElementEnabled(SEARCH_BUTTON);
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
			WebElement pagination = common.findFirstVisibleElement(PAGINATION_CONTAINER);
			WebElement nextBtn = common.findFirstVisibleElement(NEXT_PAGE_BUTTON);
			WebElement prevBtn = common.findFirstVisibleElement(PREVIOUS_PAGE_BUTTON);

			return pagination != null && pagination.isDisplayed() && nextBtn != null && prevBtn != null;
		} catch (Exception e) {
			logger.error("Pagination visibility validation failed", e);
			return false;
		}
	}

	public boolean validateNextPageWorking() {
		try {
			WebElement nextBtn = common.findFirstVisibleElement(NEXT_PAGE_BUTTON);
			if (nextBtn == null) {
				return false;
			}

			if (!nextBtn.isEnabled()) {
				logger.info("Next page button is disabled; likely single-page result.");
				return true;
			}

			String beforePageInfo = getPageInfoText();
			common.clickElement(nextBtn);
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
			WebElement prevBtn = common.findFirstVisibleElement(PREVIOUS_PAGE_BUTTON);
			WebElement nextBtn = common.findFirstVisibleElement(NEXT_PAGE_BUTTON);

			if (prevBtn == null) {
				return false;
			}

			if (!prevBtn.isEnabled() && nextBtn != null && nextBtn.isEnabled()) {
				common.clickElement(nextBtn);
				waitForTableStabilization();
				prevBtn = common.findFirstVisibleElement(PREVIOUS_PAGE_BUTTON);
			}

			if (prevBtn == null || !prevBtn.isEnabled()) {
				logger.info("Previous page button is disabled; pagination did not move ahead.");
				return true;
			}

			String beforePageInfo = getPageInfoText();
			common.clickElement(prevBtn);
			waitForTableStabilization();

			String afterPageInfo = getPageInfoText();
			return !beforePageInfo.equals(afterPageInfo);
		} catch (Exception e) {
			logger.error("Previous page validation failed", e);
			return false;
		}
	}

	public boolean validatePageNumberClick() {
		try {
			List<WebElement> pageButtons = driver.findElements(PAGE_NUMBER_BUTTONS);
			if (pageButtons.isEmpty()) {
				logger.info("No direct page number buttons found.");
				return true;
			}

			WebElement target = pageButtons.get(pageButtons.size() - 1);
			String beforePageInfo = getPageInfoText();
			common.clickElement(target);
			waitForTableStabilization();
			String afterPageInfo = getPageInfoText();

			return !beforePageInfo.equals(afterPageInfo) || target.isEnabled();
		} catch (Exception e) {
			logger.error("Page number click validation failed", e);
			return false;
		}
	}

	public boolean validatePaginationHiddenWhenNoData() {
		try {
			String query = "NO_DATA_FILTER_" + System.currentTimeMillis();
			performSearch(query);

			boolean noData = table.isNoDataImagePresent(TABLE_ROOT) || getTableRowCount() == 0;
			WebElement pagination = common.findFirstVisibleElement(PAGINATION_CONTAINER);
			boolean paginationHiddenOrDisabled = pagination == null || !pagination.isDisplayed()
					|| (!common.isElementEnabled(NEXT_PAGE_BUTTON) && !common.isElementEnabled(PREVIOUS_PAGE_BUTTON));

			resetSearch();
			return noData && paginationHiddenOrDisabled;
		} catch (Exception e) {
			logger.error("Pagination hidden when no data validation failed", e);
			return false;
		}
	}

	public boolean validateCard2IsVisible() {
		try {
			WebElement card = getKpiCardByIndex(2);
			common.highlightElement(card, "green");
			return card.isDisplayed();
		} catch (Exception e) {
			logger.error("Card 2 visibility validation failed", e);
			return false;
		}
	}

	public boolean validateCard2CountFormat() {
		try {
			WebElement card = getKpiCardByIndex(2);
			String text = card.getText();
			return text.matches("(?s).*\\d+.*");
		} catch (Exception e) {
			logger.error("Card 2 count format validation failed", e);
			return false;
		}
	}

	public boolean validateCard2Clickable() {
		try {
			String cardName = clickCardByIndexAndGetName(2);
			String header = getComponentTitle();
			return !cardName.isBlank() && !header.isBlank();
		} catch (Exception e) {
			logger.error("Card 2 click validation failed", e);
			return false;
		}
	}

	public boolean validateCard2TableVisible() {
		try {
			clickCardByIndexAndGetName(2);
			return table.isTableVisible(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 2 table visibility validation failed", e);
			return false;
		}
	}

	public boolean validateCard2TableHeaders() {
		try {
			clickCardByIndexAndGetName(2);
			return table.hasTableHeaders(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 2 table headers validation failed", e);
			return false;
		}
	}

	public boolean validateCard2TableButtons() {
		try {
			clickCardByIndexAndGetName(2);
			return table.areViewButtonsEnabled(TABLE_ROOT) && table.areDeleteButtonsEnabled(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 2 table action buttons validation failed", e);
			return false;
		}
	}

	public boolean validateCard2TableDataVisible() {
		try {
			clickCardByIndexAndGetName(2);
			return table.hasTableDataOrNoDataState(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 2 table data visibility validation failed", e);
			return false;
		}
	}

	public boolean validateCard2DownloadButtonVisible() {
		try {
			clickCardByIndexAndGetName(2);
			return common.isElementVisible(DOWNLOAD_REPORT_BTN);
		} catch (Exception e) {
			logger.error("Card 2 download button visibility validation failed", e);
			return false;
		}
	}

	private void performSearch(String query) {
		common.typeAndSearch(SEARCH_INPUT, SEARCH_BUTTON, query);
		waitForTableStabilization();
	}

	private void resetSearch() {
		common.resetSearch(SEARCH_INPUT, RESET_BUTTON);
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
		common.waitForVisibility(TABLE_ROOT);
	}

	private String getPageInfoText() {
		return common.getElementText(PAGE_INFO);
	}

	private boolean isPreviousButtonEnabled() {
		return common.isElementEnabled(PREVIOUS_PAGE_BUTTON);
	}

	private WebElement getKpiCardByIndex(int indexOneBased) {
		List<WebElement> cards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(KPI_CARDS));
		if (cards.size() < indexOneBased) {
			throw new IllegalStateException("KPI card index not found: " + indexOneBased);
		}
		WebElement card = cards.get(indexOneBased - 1);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", card);
		return card;
	}

	private String clickCardByIndexAndGetName(int indexOneBased) {
		WebElement card = getKpiCardByIndex(indexOneBased);
		String cardName = card.getText().split("\n")[0].trim();
		common.clickElement(card);
		waitForTableStabilization();
		return cardName;
	}

	private String getComponentTitle() {
		return common.getElementText(COMPONENT_TITLE);
	}

}


