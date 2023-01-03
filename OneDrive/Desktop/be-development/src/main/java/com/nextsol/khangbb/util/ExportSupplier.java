package com.nextsol.khangbb.util;

import com.nextsol.khangbb.entity.Supplier;
import com.nextsol.khangbb.exception.BadRequestException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


public class ExportSupplier {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Supplier> supplierList;

    public ExportSupplier(List<Supplier> supplierList) {
        this.supplierList = supplierList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Danh sach nha cung cap");
        Row rowMain = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 7));
        CellStyle styleMain = workbook.createCellStyle();
        XSSFFont fontMain = workbook.createFont();
        fontMain.setBold(true);
        fontMain.setFontHeight(25);
        styleMain.setFont(fontMain);
        createCell(rowMain, 4, "DANH SÁCH NHÀ CUNG CẤP", styleMain);
    }

    private void writeTtileLine() {
        Row row = sheet.createRow(2);
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
        createCell(row, 0, "STT", style);
        createCell(row, 1, "Tên nhà cung cấp", style);
        createCell(row, 2, "Địa chỉ", style);
        createCell(row, 3, "Người liên hệ", style);
        createCell(row, 4, "Điện Thoại", style);
        createCell(row, 5, "Fax", style);
        createCell(row, 6, "Email", style);
        createCell(row, 7, "Nhóm nhà cung cấp", style);
        createCell(row, 8, "Ghi chú", style);


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
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 3;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        int no = 0;
        for (Supplier supplier : supplierList) {
            if (supplier.getGroup() != null) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;
                createCell(row, columnCount++, ++no, style);
                createCell(row, columnCount++, supplier.getName(), style);
                createCell(row, columnCount++, supplier.getAddress(), style);
                createCell(row, columnCount++, supplier.getContactName(), style);
                createCell(row, columnCount++, supplier.getPhone(), style);
                createCell(row, columnCount++, supplier.getFax(), style);
                createCell(row, columnCount++, supplier.getEmail(), style);
                createCell(row, columnCount++, supplier.getGroup().getName(), style);
                createCell(row, columnCount++, supplier.getDescription(), style);
            } else {
                throw new BadRequestException(supplier.getName() + " need to add Group ID");
            }
        }
    }

    public ByteArrayInputStream export() throws IOException {
        writeHeaderLine();
        writeTtileLine();
        writeDataLines();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
