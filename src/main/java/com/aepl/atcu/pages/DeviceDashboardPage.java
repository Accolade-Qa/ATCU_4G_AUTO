package com.aepl.atcu.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.aepl.atcu.locators.DeviceDashboardPageLocators.*;
import com.aepl.atcu.util.MouseActions;
import com.aepl.atcu.util.PageActionsUtil;
import com.aepl.atcu.util.TableUtils;
import com.aepl.atcu.util.ConfigProperties;

public class DeviceDashboardPage {
	private final WebDriver driver;
	private final WebDriverWait wait;
	private final PageActionsUtil common;
	private final TableUtils table;
	private static final Logger logger = LogManager.getLogger(DeviceDashboardPage.class);
	private static final Pattern COUNT_PATTERN = Pattern.compile("(\\d+)");

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

	public String getKpiCardTitle(int indexOneBased) {
		WebElement card = getKpiCardByIndex(indexOneBased);
		String cardText = card.getText();
		if (cardText == null || cardText.isBlank()) {
			return "";
		}
		return cardText.split("\n")[0].trim();
	}

	public int getKpiCardCount(int indexOneBased) {
		WebElement card = getKpiCardByIndex(indexOneBased);
		String text = card.getText();
		if (text == null || text.isBlank()) {
			return -1;
		}
		Matcher matcher = COUNT_PATTERN.matcher(text.replace(",", ""));
		return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
	}

	public String openKpiCardAndGetComponentTitle(int indexOneBased) {
		clickCardByIndexAndGetName(indexOneBased);
		return getComponentTitle();
	}

	public List<String> getTableHeadersForKpiCard(int indexOneBased) {
		clickCardByIndexAndGetName(indexOneBased);
		return table.getTableHeaders(TABLE_ROOT);
	}

	public int getTableRowCountForKpiCard(int indexOneBased) {
		clickCardByIndexAndGetName(indexOneBased);
		return getTableRowCount();
	}

	public String getPaginationInfoForKpiCard(int indexOneBased) {
		clickCardByIndexAndGetName(indexOneBased);
		String pageInfo = getPageInfoText();
		return pageInfo == null ? "" : pageInfo.trim();
	}

	public int getTotalKpiCardCount() {
		List<WebElement> cards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(KPI_CARDS));
		return cards.size();
	}

	public int getTableHeaderCountForKpiCard(int indexOneBased) {
		return getTableHeadersForKpiCard(indexOneBased).size();
	}

	public String getDownloadButtonStateForKpiCard(int indexOneBased) {
		clickCardByIndexAndGetName(indexOneBased);
		boolean visible = common.isElementVisible(DOWNLOAD_REPORT_BTN);
		boolean enabled = common.isElementEnabled(DOWNLOAD_REPORT_BTN);
		if (!visible) {
			return "HIDDEN";
		}
		return enabled ? "VISIBLE_ENABLED" : "VISIBLE_DISABLED";
	}

	public String getSearchControlsStateForKpiCard(int indexOneBased) {
		clickCardByIndexAndGetName(indexOneBased);
		boolean inputVisible = common.isElementVisible(SEARCH_INPUT);
		boolean inputEnabled = common.isElementEnabled(SEARCH_INPUT);
		boolean buttonVisible = common.isElementVisible(SEARCH_BUTTON);
		boolean buttonEnabled = common.isElementEnabled(SEARCH_BUTTON);
		return String.format("INPUT[%s,%s]|BUTTON[%s,%s]", inputVisible, inputEnabled, buttonVisible, buttonEnabled);
	}

	public String getInvalidSearchResultStateForKpiCard(int indexOneBased) {
		clickCardByIndexAndGetName(indexOneBased);
		String query = "NO_MATCH_" + System.currentTimeMillis();
		performSearch(query);
		boolean noDataFound = table.isNoDataImagePresent(TABLE_ROOT) || getTableRowCount() == 0;
		resetSearch();
		return noDataFound ? "NO_DATA" : "HAS_DATA";
	}

	public int[] getSearchCountsForKpiCard(int indexOneBased) {
		clickCardByIndexAndGetName(indexOneBased);
		int initialCount = getTableRowCount();
		String firstValue = getFirstRowFirstValue();

		if (firstValue == null || firstValue.isBlank()) {
			return new int[] { initialCount, initialCount, initialCount };
		}

		String query = firstValue.length() > 5 ? firstValue.substring(0, 5) : firstValue;
		performSearch(query);
		int filteredCount = getTableRowCount();
		resetSearch();
		int resetCount = getTableRowCount();

		return new int[] { initialCount, filteredCount, resetCount };
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
			String searchTerm = getSavedSearchTermByCurrentComponent();
			if (searchTerm == null || searchTerm.isBlank()) {
				logger.warn("Saved deterministic search term is not configured.");
				return false;
			}

			int initialCount = getTableRowCount();
			performSearch(searchTerm);

			boolean anyRowMatched = isAnyRowContains(searchTerm);
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
			performSearch(getInvalidSearchToken());

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

			String searchTerm = getSavedSearchTermByCurrentComponent();
			if (searchTerm == null || searchTerm.isBlank()) {
				return false;
			}

			performSearch(searchTerm);

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
			performSearch(getInvalidSearchToken());

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
			return getKpiCardCount(2) >= 0;
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

	public boolean validateCard3IsVisible() {
		try {
			WebElement card = getKpiCardByIndex(3);
			common.highlightElement(card, "green");
			return card.isDisplayed();
		} catch (Exception e) {
			logger.error("Card 3 visibility validation failed", e);
			return false;
		}
	}

	public boolean validateCard3CountFormat() {
		try {
			return getKpiCardCount(3) >= 0;
		} catch (Exception e) {
			logger.error("Card 3 count format validation failed", e);
			return false;
		}
	}

	public boolean validateCard3Clickable() {
		try {
			String cardName = clickCardByIndexAndGetName(3);
			String header = getComponentTitle();
			return !cardName.isBlank() && !header.isBlank();
		} catch (Exception e) {
			logger.error("Card 3 click validation failed", e);
			return false;
		}
	}

	public boolean validateCard3TableVisible() {
		try {
			clickCardByIndexAndGetName(3);
			return table.isTableVisible(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 3 table visibility validation failed", e);
			return false;
		}
	}

	public boolean validateCard3TableHeaders() {
		try {
			clickCardByIndexAndGetName(3);
			return table.hasTableHeaders(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 3 table headers validation failed", e);
			return false;
		}
	}

	public boolean validateCard3TableButtons() {
		try {
			clickCardByIndexAndGetName(3);
			return table.areViewButtonsEnabled(TABLE_ROOT) && table.areDeleteButtonsEnabled(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 3 table action buttons validation failed", e);
			return false;
		}
	}

	public boolean validateCard3TableDataVisible() {
		try {
			clickCardByIndexAndGetName(3);
			return table.hasTableDataOrNoDataState(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 3 table data visibility validation failed", e);
			return false;
		}
	}

	public boolean validateCard3DownloadButtonVisible() {
		try {
			clickCardByIndexAndGetName(3);
			return common.isElementVisible(DOWNLOAD_REPORT_BTN);
		} catch (Exception e) {
			logger.error("Card 3 download button visibility validation failed", e);
			return false;
		}
	}

	public boolean validateCard3DownloadButtonEnabled() {
		try {
			clickCardByIndexAndGetName(3);
			return common.isElementEnabled(DOWNLOAD_REPORT_BTN);
		} catch (Exception e) {
			logger.error("Card 3 download button enabled validation failed", e);
			return false;
		}
	}

	public boolean validateCard3DownloadButtonClickable() {
		try {
			clickCardByIndexAndGetName(3);
			return validateTheDownloadButtonIsClickableOnTotalProductionDevicesTable();
		} catch (Exception e) {
			logger.error("Card 3 download button clickable validation failed", e);
			return false;
		}
	}

	public boolean validateCard4IsVisible() {
		try {
			WebElement card = getKpiCardByIndex(4);
			common.highlightElement(card, "green");
			return card.isDisplayed();
		} catch (Exception e) {
			logger.error("Card 4 visibility validation failed", e);
			return false;
		}
	}

	public boolean validateCard4CountFormat() {
		try {
			return getKpiCardCount(4) >= 0;
		} catch (Exception e) {
			logger.error("Card 4 count format validation failed", e);
			return false;
		}
	}

	public boolean validateCard4Clickable() {
		try {
			String cardName = clickCardByIndexAndGetName(4);
			String header = getComponentTitle();
			return !cardName.isBlank() && !header.isBlank();
		} catch (Exception e) {
			logger.error("Card 4 click validation failed", e);
			return false;
		}
	}

	public boolean validateCard4TableVisible() {
		try {
			clickCardByIndexAndGetName(4);
			return table.isTableVisible(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 4 table visibility validation failed", e);
			return false;
		}
	}

	public boolean validateCard4TableHeaders() {
		try {
			clickCardByIndexAndGetName(4);
			return table.hasTableHeaders(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 4 table headers validation failed", e);
			return false;
		}
	}

	public boolean validateCard4TableButtons() {
		try {
			clickCardByIndexAndGetName(4);
			return table.areViewButtonsEnabled(TABLE_ROOT) && table.areDeleteButtonsEnabled(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 4 table action buttons validation failed", e);
			return false;
		}
	}

	public boolean validateCard4TableDataVisible() {
		try {
			clickCardByIndexAndGetName(4);
			return table.hasTableDataOrNoDataState(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Card 4 table data visibility validation failed", e);
			return false;
		}
	}

	public boolean validateCard4DownloadButtonVisible() {
		try {
			clickCardByIndexAndGetName(4);
			return common.isElementVisible(DOWNLOAD_REPORT_BTN);
		} catch (Exception e) {
			logger.error("Card 4 download button visibility validation failed", e);
			return false;
		}
	}

	public boolean validateCard4DownloadButtonEnabled() {
		try {
			clickCardByIndexAndGetName(4);
			return common.isElementEnabled(DOWNLOAD_REPORT_BTN);
		} catch (Exception e) {
			logger.error("Card 4 download button enabled validation failed", e);
			return false;
		}
	}

	public boolean validateCard4DownloadButtonClickable() {
		try {
			clickCardByIndexAndGetName(4);
			return validateTheDownloadButtonIsClickableOnTotalProductionDevicesTable();
		} catch (Exception e) {
			logger.error("Card 4 download button clickable validation failed", e);
			return false;
		}
	}

	public boolean isGraphWidgetVisible(int indexOneBased) {
		try {
			return getGraphWidgetByIndex(indexOneBased).isDisplayed();
		} catch (Exception e) {
			logger.error("Graph {} visibility validation failed", indexOneBased, e);
			return false;
		}
	}

	public String getGraphTitle(int indexOneBased) {
		try {
			WebElement graph = getGraphWidgetByIndex(indexOneBased);
			List<WebElement> titles = graph.findElements(GRAPH_TITLE_INNER);
			for (WebElement title : titles) {
				String text = title.getText().trim();
				if (!text.isBlank()) {
					return text;
				}
			}
			String fallback = graph.getText();
			if (fallback == null || fallback.isBlank()) {
				return "";
			}
			return fallback.split("\n")[0].trim();
		} catch (Exception e) {
			logger.error("Graph {} title fetch failed", indexOneBased, e);
			return "";
		}
	}

	public boolean isGraphLegendVisible(int indexOneBased) {
		try {
			WebElement graph = getGraphWidgetByIndex(indexOneBased);
			List<WebElement> legends = graph.findElements(GRAPH_LEGEND_INNER);
			for (WebElement legend : legends) {
				if (legend.isDisplayed()) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("Graph {} legend visibility validation failed", indexOneBased, e);
			return false;
		}
	}

	public boolean isGraphTooltipVisibleOnHover(int indexOneBased) {
		try {
			WebElement graph = getGraphWidgetByIndex(indexOneBased);
			new Actions(driver).moveToElement(graph).perform();
			List<WebElement> tooltips = driver.findElements(GRAPH_TOOLTIP_GENERIC);
			for (WebElement tooltip : tooltips) {
				if (tooltip.isDisplayed()) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			logger.error("Graph {} tooltip visibility validation failed", indexOneBased, e);
			return false;
		}
	}

	public boolean isGraphClickable(int indexOneBased) {
		try {
			WebElement graph = getGraphWidgetByIndex(indexOneBased);
			common.clickElement(graph);
			return true;
		} catch (Exception e) {
			logger.error("Graph {} click validation failed", indexOneBased, e);
			return false;
		}
	}

	public String clickGraphAndGetOpenedComponentTitle(int indexOneBased) {
		try {
			WebElement graph = getGraphWidgetByIndex(indexOneBased);
			common.clickElement(graph);
			waitForTableStabilization();
			return getComponentTitle();
		} catch (Exception e) {
			logger.error("Graph {} click/open title validation failed", indexOneBased, e);
			return "";
		}
	}

	public boolean isGraphTableVisible(int indexOneBased) {
		try {
			clickGraphAndGetOpenedComponentTitle(indexOneBased);
			return table.isTableVisible(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Graph {} table visibility validation failed", indexOneBased, e);
			return false;
		}
	}

	public boolean hasGraphTableHeaders(int indexOneBased) {
		try {
			clickGraphAndGetOpenedComponentTitle(indexOneBased);
			return table.hasTableHeaders(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Graph {} table headers validation failed", indexOneBased, e);
			return false;
		}
	}

	public boolean hasGraphTableDataOrNoDataState(int indexOneBased) {
		try {
			clickGraphAndGetOpenedComponentTitle(indexOneBased);
			return table.hasTableDataOrNoDataState(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Graph {} table data/no-data validation failed", indexOneBased, e);
			return false;
		}
	}

	public boolean hasGraphTableActionButtonsState(int indexOneBased) {
		try {
			clickGraphAndGetOpenedComponentTitle(indexOneBased);
			if (table.isNoDataImagePresent(TABLE_ROOT)) {
				return true;
			}
			return table.areViewButtonsEnabled(TABLE_ROOT) && table.areDeleteButtonsEnabled(TABLE_ROOT);
		} catch (Exception e) {
			logger.error("Graph {} table action buttons validation failed", indexOneBased, e);
			return false;
		}
	}

	public String getGraphTableDownloadState(int indexOneBased) {
		try {
			clickGraphAndGetOpenedComponentTitle(indexOneBased);
			boolean visible = common.isElementVisible(DOWNLOAD_REPORT_BTN);
			boolean enabled = common.isElementEnabled(DOWNLOAD_REPORT_BTN);
			if (!visible) {
				return "HIDDEN";
			}
			return enabled ? "VISIBLE_ENABLED" : "VISIBLE_DISABLED";
		} catch (Exception e) {
			logger.error("Graph {} download state validation failed", indexOneBased, e);
			return "ERROR";
		}
	}

	public String getGraphTableSearchState(int indexOneBased) {
		try {
			clickGraphAndGetOpenedComponentTitle(indexOneBased);
			boolean inputVisible = common.isElementVisible(SEARCH_INPUT);
			boolean inputEnabled = common.isElementEnabled(SEARCH_INPUT);
			boolean buttonVisible = common.isElementVisible(SEARCH_BUTTON);
			boolean buttonEnabled = common.isElementEnabled(SEARCH_BUTTON);
			return String.format("INPUT[%s,%s]|BUTTON[%s,%s]", inputVisible, inputEnabled, buttonVisible, buttonEnabled);
		} catch (Exception e) {
			logger.error("Graph {} search state validation failed", indexOneBased, e);
			return "ERROR";
		}
	}

	public boolean validateGraphTableSearchWithValidData(int indexOneBased) {
		try {
			clickGraphAndGetOpenedComponentTitle(indexOneBased);
			return validateSearchWithValidData();
		} catch (Exception e) {
			logger.error("Graph {} valid search validation failed", indexOneBased, e);
			return false;
		}
	}

	public String getGraphTableInvalidSearchState(int indexOneBased) {
		try {
			clickGraphAndGetOpenedComponentTitle(indexOneBased);
			performSearch(getInvalidSearchToken());
			boolean noDataFound = table.isNoDataImagePresent(TABLE_ROOT) || getTableRowCount() == 0;
			resetSearch();
			return noDataFound ? "NO_DATA" : "HAS_DATA";
		} catch (Exception e) {
			logger.error("Graph {} invalid search validation failed", indexOneBased, e);
			return "ERROR";
		}
	}

	public String getGraphTablePaginationState(int indexOneBased) {
		try {
			clickGraphAndGetOpenedComponentTitle(indexOneBased);
			return getPageInfoText();
		} catch (Exception e) {
			logger.error("Graph {} pagination visibility validation failed", indexOneBased, e);
			return "";
		}
	}

	public boolean validateGraphTableNextPagination(int indexOneBased) {
		try {
			clickGraphAndGetOpenedComponentTitle(indexOneBased);
			return validateNextPageWorking();
		} catch (Exception e) {
			logger.error("Graph {} next pagination validation failed", indexOneBased, e);
			return false;
		}
	}

	public boolean validateGraphTablePreviousPagination(int indexOneBased) {
		try {
			clickGraphAndGetOpenedComponentTitle(indexOneBased);
			return validatePreviousPageWorking();
		} catch (Exception e) {
			logger.error("Graph {} previous pagination validation failed", indexOneBased, e);
			return false;
		}
	}

	public boolean isFooterVisible() {
		try {
			WebElement footer = wait.until(ExpectedConditions
					.visibilityOfElementLocated(com.aepl.atcu.locators.CommonPageLocators.FOOTER));
			return footer.isDisplayed();
		} catch (Exception e) {
			logger.error("Footer visibility validation failed", e);
			return false;
		}
	}

	public String getFooterVersionText() {
		try {
			return common.getElementText(com.aepl.atcu.locators.CommonPageLocators.VERSION);
		} catch (Exception e) {
			logger.error("Footer version text fetch failed", e);
			return "";
		}
	}

	public String getFooterCopyrightText() {
		try {
			return common.getElementText(com.aepl.atcu.locators.CommonPageLocators.COPYRIGHT);
		} catch (Exception e) {
			logger.error("Footer copyright text fetch failed", e);
			return "";
		}
	}

	public boolean isFooterAlignmentValid() {
		try {
			WebElement footer = wait.until(ExpectedConditions
					.visibilityOfElementLocated(com.aepl.atcu.locators.CommonPageLocators.FOOTER));
			return footer.getSize().getWidth() > 0 && footer.getLocation().getY() >= 0;
		} catch (Exception e) {
			logger.error("Footer alignment validation failed", e);
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

	private WebElement getGraphWidgetByIndex(int indexOneBased) {
		List<WebElement> graphs = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(GRAPH_WIDGETS));
		List<WebElement> visibleGraphs = new ArrayList<>();
		for (WebElement graph : graphs) {
			try {
				if (graph.isDisplayed() && graph.getSize().getHeight() > 40 && graph.getSize().getWidth() > 40) {
					visibleGraphs.add(graph);
				}
			} catch (Exception ignored) {
			}
		}
		if (visibleGraphs.size() < indexOneBased) {
			throw new IllegalStateException("Graph widget index not found: " + indexOneBased);
		}
		WebElement graph = visibleGraphs.get(indexOneBased - 1);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", graph);
		return graph;
	}

	public int getTotalGraphCount() {
		try {
			List<WebElement> graphs = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(GRAPH_WIDGETS));
			int count = 0;
			for (WebElement graph : graphs) {
				if (graph.isDisplayed() && graph.getSize().getHeight() > 40 && graph.getSize().getWidth() > 40) {
					count++;
				}
			}
			return count;
		} catch (Exception e) {
			logger.error("Unable to determine total graph count", e);
			return 0;
		}
	}

	private String getSavedSearchTermByCurrentComponent() {
		String componentTitle = getComponentTitle().toLowerCase();
		String productionImei = ConfigProperties.getProperty("dashboard.search.production.imei");
		String dispatchedImei = ConfigProperties.getProperty("dashboard.search.dispatched.imei");
		String installedImei = ConfigProperties.getProperty("dashboard.search.installed.imei");
		String discardedUin = ConfigProperties.getProperty("dashboard.search.discarded.uin");
		String activityImei = ConfigProperties.getProperty("dashboard.search.activity.imei");
		String firmwareImei = ConfigProperties.getProperty("dashboard.search.firmware.imei");

		if (componentTitle.contains("production")) {
			return productionImei;
		}
		if (componentTitle.contains("dispatched")) {
			return dispatchedImei;
		}
		if (componentTitle.contains("installed")) {
			return installedImei;
		}
		if (componentTitle.contains("discarded")) {
			return discardedUin;
		}
		if (componentTitle.contains("activity")) {
			return activityImei;
		}
		if (componentTitle.contains("firmware")) {
			return firmwareImei;
		}
		return productionImei;
	}

	private String getInvalidSearchToken() {
		String configured = ConfigProperties.getProperty("dashboard.search.invalid.token");
		if (configured == null || configured.isBlank()) {
			return "INVALID_DEVICE_TOKEN_NOT_PRESENT";
		}
		return configured;
	}

}


