package com.example.expensetracker.service;

import com.example.expensetracker.entity.Expense;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportExpensesToExcel(List<Expense> expenses) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Sheet 1: All Expenses
            Sheet expenseSheet = workbook.createSheet("All Expenses");
            createExpenseSheet(workbook, expenseSheet, expenses);

            // Sheet 2: Summary by Category
            Sheet summarySheet = workbook.createSheet("Category Summary");
            createSummarySheet(workbook, summarySheet, expenses);

            // Sheet 3: Monthly Summary
            Sheet monthlySheet = workbook.createSheet("Monthly Summary");
            createMonthlySheet(workbook, monthlySheet, expenses);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to Excel: " + e.getMessage(), e);
        }
    }

    private void createExpenseSheet(Workbook workbook, Sheet sheet, List<Expense> expenses) {
        // Create header style
        CellStyle headerStyle = createHeaderStyle(workbook);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Title", "Amount (Rs)", "Category", "Payment Method", "Date"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        int rowNum = 1;
        DecimalFormat df = new DecimalFormat("0.00");

        for (Expense expense : expenses) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(expense.getId());
            row.createCell(1).setCellValue(expense.getTitle());
            row.createCell(2).setCellValue(df.format(expense.getAmount()));
            row.createCell(3).setCellValue(expense.getCategory());
            row.createCell(4).setCellValue(expense.getPaymentMethod());
            row.createCell(5).setCellValue(expense.getExpenseDate() != null ?
                    expense.getExpenseDate().toString() : "");
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Add total row
        if (!expenses.isEmpty()) {
            Row totalRow = sheet.createRow(rowNum);
            Cell totalLabelCell = totalRow.createCell(1);
            totalLabelCell.setCellValue("TOTAL");

            CellStyle boldStyle = createBoldStyle(workbook);
            totalLabelCell.setCellStyle(boldStyle);

            double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
            Cell totalAmountCell = totalRow.createCell(2);
            totalAmountCell.setCellValue(df.format(total));
            totalAmountCell.setCellStyle(boldStyle);
        }
    }

    private void createSummarySheet(Workbook workbook, Sheet sheet, List<Expense> expenses) {
        CellStyle headerStyle = createHeaderStyle(workbook);

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Category");
        headerRow.createCell(1).setCellValue("Total Amount (Rs)");
        headerRow.createCell(2).setCellValue("Count");

        for (int i = 0; i < 3; i++) {
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        Map<String, Double> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));

        Map<String, Long> categoryCounts = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.counting()
                ));

        int rowNum = 1;
        DecimalFormat df = new DecimalFormat("0.00");

        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(df.format(entry.getValue()));
            row.createCell(2).setCellValue(categoryCounts.get(entry.getKey()));
        }

        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createMonthlySheet(Workbook workbook, Sheet sheet, List<Expense> expenses) {
        CellStyle headerStyle = createHeaderStyle(workbook);

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Month-Year");
        headerRow.createCell(1).setCellValue("Total Amount (Rs)");
        headerRow.createCell(2).setCellValue("Count");

        for (int i = 0; i < 3; i++) {
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        Map<String, Double> monthlyTotals = expenses.stream()
                .filter(e -> e.getExpenseDate() != null)
                .collect(Collectors.groupingBy(
                        expense -> expense.getExpenseDate().getYear() + "-" +
                                String.format("%02d", expense.getExpenseDate().getMonthValue()),
                        Collectors.summingDouble(Expense::getAmount)
                ));

        Map<String, Long> monthlyCounts = expenses.stream()
                .filter(e -> e.getExpenseDate() != null)
                .collect(Collectors.groupingBy(
                        expense -> expense.getExpenseDate().getYear() + "-" +
                                String.format("%02d", expense.getExpenseDate().getMonthValue()),
                        Collectors.counting()
                ));

        int rowNum = 1;
        DecimalFormat df = new DecimalFormat("0.00");

        for (Map.Entry<String, Double> entry : monthlyTotals.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(df.format(entry.getValue()));
            row.createCell(2).setCellValue(monthlyCounts.get(entry.getKey()));
        }

        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        return headerStyle;
    }

    private CellStyle createBoldStyle(Workbook workbook) {
        CellStyle boldStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);
        return boldStyle;
    }
}