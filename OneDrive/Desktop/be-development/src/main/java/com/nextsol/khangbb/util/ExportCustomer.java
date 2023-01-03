package com.nextsol.khangbb.util;

import com.nextsol.khangbb.entity.Customers;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ExportCustomer {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Customers> customersList;

    public ExportCustomer(List<Customers> customersList) {
        this.customersList = customersList;
        workbook = new XSSFWorkbook();

    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Danh sach khach hang");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFillBackgroundColor(IndexedColors.YELLOW.index);
        createCell(row, 0, "Tên khách hàng", style);
        createCell(row, 1, "Số điện thoại", style);
        createCell(row, 2, "Địa chỉ", style);
        createCell(row, 3, "Ngày sinh", style);
        createCell(row, 4, "Giới tính", style);
        createCell(row, 5, "Ghi chú", style);
        createCell(row, 6, "Nhóm", style);
        createCell(row, 7, "Email", style);
        createCell(row, 8, "Doanh số", style);
        createCell(row, 9, "Số lần mua hàng", style);
        createCell(row, 10, "Điểm tích lũy", style);
        createCell(row, 11, "Dư nợ", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(String.valueOf((BigDecimal) value));
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else {
            cell.setCellValue((Date) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        for (Customers customers : customersList) {
            if (customers.getCustomerGroup().getId() != null) {
                String born = DateUtil.formattedDate(customers.getBirthday());
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                createCell(row, columnCount++, customers.getFullname(), style);
                createCell(row, columnCount++, customers.getPhone(), style);
                createCell(row, columnCount++, customers.getAddress(), style);
                createCell(row, columnCount++, born, style);
                createCell(row, columnCount++, customers.getSex(), style);
                createCell(row, columnCount++, customers.getDescription(), style);
                createCell(row, columnCount++, customers.getCustomerGroup().getName(), style);
                createCell(row, columnCount++, customers.getEmail(), style);
                createCell(row, columnCount++, customers.getTurnover(), style);
                createCell(row, columnCount++, customers.getNumberPurchase(), style);
                createCell(row, columnCount++, customers.getPoint(), style);
                createCell(row, columnCount++, customers.getDebit(), style);
            }
        }
    }

    public ByteArrayInputStream export() throws IOException {
        writeHeaderLine();
        writeDataLines();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
