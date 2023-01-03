package com.nextsol.khangbb.util;


import com.nextsol.khangbb.model.PurchaseHistoryDTO;
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

public class ExportDebitCustomer {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<PurchaseHistoryDTO> dtoList;

    public ExportDebitCustomer(List<PurchaseHistoryDTO> dtoList) {
        this.dtoList = dtoList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Bao cao chi tiet cong no");
        Row rowMain = sheet.createRow(0);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 5));
        CellStyle styleMain = workbook.createCellStyle();
        XSSFFont fontMain = workbook.createFont();
        fontMain.setBold(true);
        fontMain.setFontHeight(25);
        styleMain.setFont(fontMain);
        createCell(rowMain, 4, "Công Nợ Khách Hàng", styleMain);
    }

    private void writeHeaderCustomer() {
        Row row = sheet.createRow(1);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(11);
        style.setFont(font);
        createCell(row, 4, "Khách hàng", style);
        createCell(row, 5, dtoList.get(0).getNameCustomer(), style);
    }
    private void writeHeaderDebit() {
        Row row = sheet.createRow(2);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(11);
        style.setFont(font);
        createCell(row, 4, "Dư nợ", style);
        createCell(row, 5, dtoList.get(0).getTotalPrice(), style);
    }



    private void writeHeaderLine() {
        Row row = sheet.createRow(3);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN1.getIndex());
        style.setFillPattern(FillPatternType.FINE_DOTS);
        createCell(row, 0, "STT", style);
        createCell(row, 1, "Ngày tạo", style);
        createCell(row, 2, "Số chứng từ ", style);
        createCell(row, 3, "Phương thức ", style);
        createCell(row, 4, "Số phiếu tham chiếu", style);
        createCell(row, 5, "Số tiền thanh toán(VNĐ)", style);

    }

    private void writeDataLines() {
        int rowCount = 4;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(11);
        style.setFont(font);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        int no = 0;
        for (PurchaseHistoryDTO dto : dtoList) {
            String createDay = DateUtil.formattedDate(dto.getCreatedDate());
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, ++no, style);
            createCell(row, columnCount++, createDay, style);
            createCell(row, columnCount++, dto.getCard(), style);
            createCell(row, columnCount++, dto.getTypePayment(), style);
            createCell(row, columnCount++, null, style);
            createCell(row, columnCount++, dto.getPrice(), style);
        }
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

    public ByteArrayInputStream export() throws IOException {
        writeHeader();
        writeHeaderCustomer();
        writeHeaderDebit();
        writeHeaderLine();
        writeDataLines();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
