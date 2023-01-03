package com.nextsol.khangbb.util;

import com.nextsol.khangbb.entity.Purchase_History;
import com.nextsol.khangbb.entity.Users;
import com.nextsol.khangbb.model.paymentSlipModel.FilterRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExportPaymentSlip {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Purchase_History> list;
    private Users user;

    public ExportPaymentSlip(List<Purchase_History> purchase_histories, Users user) {
        this.list = purchase_histories;
        workbook = new XSSFWorkbook();
        this.user = user;
    }

    private void writeHeader(FilterRequest request) {
        sheet = workbook.createSheet("Báo cáo thu chi");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(6, 6, 0, 8));
        CellStyle style = getCellStyle(true, 14, HorizontalAlignment.CENTER);
        createCell(sheet.createRow(0), 0, "Báo cáo thu chi", style);
        style = getCellStyle(true, 12, HorizontalAlignment.CENTER);
        createCell(sheet.createRow(1), 0, "Cửa hàng: Khangbaby", style);
        createCell(sheet.createRow(2), 0, "Địa chỉ: " + user.getBranch().getAddress(), style);
        createCell(sheet.createRow(6), 0, "Từ ngày: " + request.getFromDate() + " đến ngày: " + request.getToDate(), style);
    }

    private void writeTitle() {
        Row row = sheet.createRow(8);
        CellStyle style = getCellStyleTitle(true, 11, HorizontalAlignment.CENTER);
        createCell(row, 0, "STT", style);
        createCell(row, 1, "Số phiếu", style);
        createCell(row, 2, "Thời gian", style);
        createCell(row, 3, "Hình thức", style);
        createCell(row, 4, "Loại", style);
        createCell(row, 5, "Lí do", style);
        createCell(row, 6, "Người thu", style);
        createCell(row, 7, "Số tiền", style);
        createCell(row, 8, "Mô tả", style);
    }

    private void writeData() {
        int rowCount = 9;
        CellStyle style = getCellStyle(false, 11, HorizontalAlignment.LEFT);
        int count = 1;
        for (Purchase_History item : list) {
            Row row = sheet.createRow(rowCount++);
            createCell(row, 0, count++, style);
            createCell(row, 1, item.getBill().getCode(), style);
            createCell(row, 2, new SimpleDateFormat("HH:mm dd/MM/yyyy").format(item.getCreatedDate()), style);
            createCell(row, 3, item.getTypePayment(), style);
            createCell(row, 4, item.getType(), style);
            createCell(row, 5, item.getReason(), style);
            createCell(row, 6, item.getSeller(), style);
            createCell(row, 7, item.getPrice(), style);
            createCell(row, 8, item.getDescription(), style);
        }
    }

    public ByteArrayInputStream export(FilterRequest filterRequest) throws IOException {
        writeHeader(filterRequest);
        writeTitle();
        writeData();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
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
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public CellStyle getCellStyle(boolean bold, int fontSize, HorizontalAlignment alignment) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(bold);
        font.setFontHeight(fontSize);
        style.setFont(font);
        style.setAlignment(alignment);
        return style;
    }

    public CellStyle getCellStyleTitle(boolean bold, int fontSize, HorizontalAlignment alignment) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(bold);
        font.setFontHeight(fontSize);
        style.setFont(font);
        style.setAlignment(alignment);
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}
