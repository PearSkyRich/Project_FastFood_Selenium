package com.fastfood.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExcelReport {

    private static final Workbook workbook;
    private static final Sheet sheet;
    private static int rowIndex = 1;

    static {

        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Login Test Report");

        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Test Case ID");
        header.createCell(1).setCellValue("Priority");
        header.createCell(2).setCellValue("Title");
        header.createCell(3).setCellValue("Test Step");
        header.createCell(4).setCellValue("Test Data");
        header.createCell(5).setCellValue("Expected Result");
        header.createCell(6).setCellValue("Actual Result");
        header.createCell(7).setCellValue("Execution Time (ms)");
        header.createCell(8).setCellValue("Status");

        // In đậm tiêu đề
        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);

        style.setFont(font);

        for (int i = 0; i <= 8; i++) {
            header.getCell(i).setCellStyle(style);
        }
    }

    /**
     * Ghi một dòng kết quả test
     */
    public static synchronized void writeResult(
            String testCaseId,
            String priority,
            String title,
            String testStep,
            String testData,
            String expectedResult,
            String actualResult,
            long executionTime,
            String status
    ) {

        Row row = sheet.createRow(rowIndex++);

        row.createCell(0).setCellValue(testCaseId);
        row.createCell(1).setCellValue(priority);
        row.createCell(2).setCellValue(title);
        row.createCell(3).setCellValue(testStep);
        row.createCell(4).setCellValue(testData);
        row.createCell(5).setCellValue(expectedResult);
        row.createCell(6).setCellValue(actualResult);
        row.createCell(7).setCellValue(executionTime);
        row.createCell(8).setCellValue(status);

    }

    /**
     * Lưu file Excel
     */
    public static void saveReport() {

        try {

            File folder = new File("reports");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String time =
                    LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String fileName =
                    "reports/Login_Report_" + time + ".xlsx";

            for (int i = 0; i <= 8; i++) {
                sheet.autoSizeColumn(i);
            }

            FileOutputStream outputStream =
                    new FileOutputStream(fileName);

            workbook.write(outputStream);

            outputStream.close();

            workbook.close();

            System.out.println();
            System.out.println("=====================================");
            System.out.println("Excel report generated successfully!");
            System.out.println(new File(fileName).getAbsolutePath());
            System.out.println("=====================================");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}