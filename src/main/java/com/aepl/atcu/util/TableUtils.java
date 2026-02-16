package com.aepl.atcu.util;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TableUtils {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final Logger logger = LogManager.getLogger(TableUtils.class);

    private static final By HEADER_LOCATOR = By.xpath(".//thead//th");
    private static final By FALLBACK_HEADER_LOCATOR = By.xpath(".//tr[1]/*");
    private static final By ROWS_LOCATOR = By.xpath(".//tbody/tr");
    private static final By NO_DATA_IMG = By.xpath(".//img[contains(@class,'no-data-img')]");

    public TableUtils(WebDriver driver, Duration timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
    }

    public TableUtils(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // ================= HEADERS =================

    public boolean isTableVisible(By tableLocator) {
        try {
            WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));
            return table.isDisplayed();
        } catch (Exception e) {
            logger.error("Table visibility check failed for locator: {}", tableLocator, e);
            return false;
        }
    }

    public List<String> getTableHeaders(By tableLocator) {
        try {
            WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));

            List<WebElement> headers = table.findElements(HEADER_LOCATOR);
            if (headers.isEmpty()) {
                headers = table.findElements(FALLBACK_HEADER_LOCATOR);
            }

            List<String> headerTexts = headers.stream()
                    .map(e -> e.getText().trim())
                    .filter(text -> !text.isEmpty())
                    .collect(Collectors.toList());

            logger.debug("Headers extracted: {}", headerTexts);
            return headerTexts;

        } catch (TimeoutException e) {
            logger.error("Table not visible: {}", tableLocator, e);
            return Collections.emptyList();
        }
    }

    public boolean hasTableHeaders(By tableLocator) {
        return !getTableHeaders(tableLocator).isEmpty();
    }

    // ================= TABLE DATA =================

    public List<Map<String, String>> getTableData(By tableLocator, List<String> headers) {
        List<Map<String, String>> tableData = new ArrayList<>();

        try {
            WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));

            if (!table.findElements(NO_DATA_IMG).isEmpty()) {
                logger.debug("No data image present. Returning empty table data.");
                return tableData;
            }

            List<WebElement> rows = table.findElements(ROWS_LOCATOR);
            logger.debug("Row count found: {}", rows.size());

            for (WebElement row : rows) {

                if (!row.findElements(NO_DATA_IMG).isEmpty()) {
                    continue;
                }

                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.isEmpty()) continue;

                Map<String, String> rowData = new LinkedHashMap<>();

                for (int i = 0; i < cells.size(); i++) {
                    String header = (i < headers.size()) ? headers.get(i).trim() : "EXTRA_COLUMN_" + (i + 1);
                    String value = extractCellValue(cells.get(i));
                    rowData.put(header, value);
                }

                tableData.add(rowData);
            }

            logger.debug("Total rows extracted: {}", tableData.size());

        } catch (Exception e) {
            logger.error("Error extracting table data", e);
        }

        return tableData;
    }

    public boolean hasTableDataOrNoDataState(By tableLocator) {
        try {
            List<String> headers = getTableHeaders(tableLocator);
            List<Map<String, String>> rows = getTableData(tableLocator, headers);
            return !rows.isEmpty() || isNoDataImagePresent(tableLocator);
        } catch (Exception e) {
            logger.error("Error validating table data/no-data state for locator: {}", tableLocator, e);
            return false;
        }
    }

    private String extractCellValue(WebElement cell) {

        // Checkbox handling
        List<WebElement> checkboxes = cell.findElements(By.cssSelector("input[type='checkbox']"));
        if (!checkboxes.isEmpty()) {
            return checkboxes.get(0).isSelected() ? "CHECKED" : "UNCHECKED";
        }

        String text = cell.getText().trim();
        if (!text.isEmpty()) return text;

        // Fallback to inner elements
        for (WebElement inner : cell.findElements(By.xpath(".//*"))) {
            String innerText = inner.getText().trim();
            if (!innerText.isEmpty()) return innerText;
        }

        return "";
    }

    // ================= BUTTON STATE =================

    public boolean areViewButtonsEnabled(By tableLocator) {
        return areActionButtonsEnabled(tableLocator, "visibility");
    }

    public boolean areDeleteButtonsEnabled(By tableLocator) {
        return areActionButtonsEnabled(tableLocator, "delete");
    }

    private boolean areActionButtonsEnabled(By tableLocator, String keyword) {
        try {
            WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));

            List<WebElement> buttons = table.findElements(
                    By.xpath(".//tbody//tr//td[last()]//button[contains(., '" + keyword + "')]"));

            logger.debug("Found {} '{}' buttons", buttons.size(), keyword);

            for (WebElement btn : buttons) {
                if (!btn.isDisplayed() || !btn.isEnabled()) {
                    logger.warn("{} button disabled or hidden", keyword);
                    return false;
                }
            }
            return true;

        } catch (Exception e) {
            logger.error("Error checking {} button state", keyword, e);
            return false;
        }
    }

    // ================= BUTTON ACTIONS =================

    public boolean clickFirstViewButton(By tableLocator) {
        return clickFirstActionButton(tableLocator, "visibility");
    }

    public boolean clickFirstDeleteButton(By tableLocator) {
        return clickFirstActionButton(tableLocator, "delete");
    }

    private boolean clickFirstActionButton(By tableLocator, String keyword) {
        try {
            WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));

            WebElement button = table.findElement(
                    By.xpath(".//tbody//tr[1]//td[last()]//button[contains(., '" + keyword + "')]"));

            wait.until(ExpectedConditions.elementToBeClickable(button)).click();

            logger.debug("Clicked first '{}' button", keyword);
            return true;

        } catch (Exception e) {
            logger.error("Error clicking first {} button", keyword, e);
            return false;
        }
    }

    // ================= NO DATA CHECK =================

    public boolean isNoDataImagePresent(By tableLocator) {
        try {
            WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(tableLocator));

            if (!table.findElements(NO_DATA_IMG).isEmpty()) return true;

            return !table.findElements(
                    By.xpath(".//*[contains(translate(text(),'NO DATA','no data'),'no data')]")).isEmpty();

        } catch (Exception e) {
            logger.error("Error checking no-data state", e);
            return false;
        }
    }
}
