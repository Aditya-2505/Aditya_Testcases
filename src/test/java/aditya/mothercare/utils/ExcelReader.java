package aditya.mothercare.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads test data from an .xlsx file.
 * The first row is treated as the header (column names).
 */
public class ExcelReader {

    private final String filePath;
    private final String sheetName;

    public ExcelReader(String filePath, String sheetName) {
        this.filePath = filePath;
        this.sheetName = sheetName;
    }

    /**
     * Returns all data rows as a list of maps (column name → cell value).
     */
    public List<Map<String, String>> getAllRows() {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                LoggerHandler.error("Sheet not found: " + sheetName + " in " + filePath);
                return data;
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return data;

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValue(cell));
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowMap = new LinkedHashMap<>();
                boolean hasData = false;
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String value = getCellValue(cell);
                    rowMap.put(headers.get(j), value);
                    if (!value.isEmpty()) hasData = true;
                }
                if (hasData) data.add(rowMap);
            }

        } catch (IOException e) {
            LoggerHandler.error("Failed to read Excel file: " + filePath, e);
        }

        return data;
    }

    /**
     * Returns data for a specific test case ID (looks up the TestCaseId column).
     */
    public Map<String, String> getRowById(String testCaseId) {
        for (Map<String, String> row : getAllRows()) {
            if (testCaseId.equalsIgnoreCase(row.get("TestCaseId"))) {
                return row;
            }
        }
        LoggerHandler.warn("No data row found for test case: " + testCaseId);
        return new LinkedHashMap<>();
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) yield cell.getDateCellValue().toString();
                double d = cell.getNumericCellValue();
                yield (d == Math.floor(d)) ? String.valueOf((long) d) : String.valueOf(d);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default      -> "";
        };
    }
}
