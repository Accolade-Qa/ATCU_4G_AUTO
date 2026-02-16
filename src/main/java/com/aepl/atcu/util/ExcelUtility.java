package com.aepl.atcu.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {

    private static final Logger logger = LogManager.getLogger(ExcelUtility.class);

    private Workbook workbook;
    private Sheet sheet;
    private String filePath;

    private CellStyle headerStyle;
    private CellStyle passStyle;
    private CellStyle failStyle;

    public void initializeExcel(String sheetName) {
        filePath = System.getProperty("user.dir") + "/test-results/" + sheetName + ".xlsx";
        Path path = Paths.get(filePath);

        try {
            Files.createDirectories(path.getParent());

            if (Files.exists(path) && Files.size(path) > 0) {
                try (FileInputStream fis = new FileInputStream(filePath)) {
                    workbook = new XSSFWorkbook(fis);
                    sheet = workbook.getSheet(sheetName) != null
                            ? workbook.getSheet(sheetName)
                            : workbook.createSheet(sheetName);
                }
                logger.debug("Loaded existing Excel file: {}", filePath);
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet(sheetName);
                createHeaderRow();
                logger.debug("Created new Excel file: {}", filePath);
            }

            createStyles();

        } catch (IOException e) {
            logger.error("Failed to initialize Excel file", e);
            throw new RuntimeException("Excel initialization failed", e);
        }
    }

    private void createHeaderRow() {
        Row headerRow = sheet.createRow(0);
        String[] headers = { "Test Case Name", "Expected Message", "Actual Message", "Status" };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private void createStyles() {
        headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        passStyle = workbook.createCellStyle();
        Font passFont = workbook.createFont();
        passFont.setColor(IndexedColors.GREEN.getIndex());
        passStyle.setFont(passFont);

        failStyle = workbook.createCellStyle();
        Font failFont = workbook.createFont();
        failFont.setColor(IndexedColors.RED.getIndex());
        failStyle.setFont(failFont);
    }

    public synchronized void writeTestDataToExcel(String testCaseName, String expected, String actual, String status) {
        if (workbook == null || sheet == null) {
            throw new IllegalStateException("Excel not initialized. Call initializeExcel() first.");
        }

        int rowNum = sheet.getLastRowNum() + 1;
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(testCaseName);
        row.createCell(1).setCellValue(expected);
        row.createCell(2).setCellValue(actual);

        Cell statusCell = row.createCell(3);
        statusCell.setCellValue(status);

        if ("PASS".equalsIgnoreCase(status)) {
            statusCell.setCellStyle(passStyle);
        } else if ("FAIL".equalsIgnoreCase(status)) {
            statusCell.setCellStyle(failStyle);
        }

        logger.debug("Added row {} for test case {}", rowNum, testCaseName);
    }

    public synchronized void saveAndClose() {
        if (workbook == null) return;

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            workbook.close();
            logger.debug("Excel file saved and closed: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to save Excel file", e);
            throw new RuntimeException("Excel save failed", e);
        }
    }
}
