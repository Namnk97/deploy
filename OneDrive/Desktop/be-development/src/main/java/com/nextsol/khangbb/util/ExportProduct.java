package com.nextsol.khangbb.util;


import com.nextsol.khangbb.entity.ProductExcel;
import com.nextsol.khangbb.entity.Product_GroupExcel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class ExportProduct {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private final List<ProductExcel> productsList;

    private final List<Product_GroupExcel> list;

    public ExportProduct(List<ProductExcel> productsList, List<Product_GroupExcel> list) {
        this.productsList = productsList;
        this.list = list;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Danh sach san pham xuat");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setFillBackgroundColor(IndexedColors.YELLOW.index);
        createCell(row, 0, "Mã sản phẩm", style);
        createCell(row, 1, "Tên sản phẩm", style);
        createCell(row, 2, "Đơn vị tính", style);
        createCell(row, 3, "Mã nhóm", style);
        createCell(row, 4, "Giá nhập hàng", style);
        createCell(row, 5, "Giá bán lẻ", style);
        createCell(row, 6, "Giá bán sỉ", style);
        createCell(row, 7, "% CK bán lẻ", style);
        createCell(row, 8, "% CK bán sỉ", style);
        createCell(row, 9, "Tiền CK bán lẻ", style);
        createCell(row, 10, "Tiền CK bán sỉ", style);
        createCell(row, 11, "Số lượng tồn tối thiểu", style);
        createCell(row, 12, "Ghi chú", style);


    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {

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
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        Map<Long, String> listCode = new HashMap<>();
        list.forEach(x -> listCode.put(x.getId(), x.getCode()));
        for (ProductExcel products : productsList) {
            Row row = sheet.createRow(rowCount++);
            createCell(row, 0, products.getCode(), style);
            createCell(row, 1, products.getName(), style);
            createCell(row, 2, products.getUnit(), style);
            createCell(row, 3, listCode.get(products.getId()), style);
            createCell(row, 4, products.getImportPrice(), style);
            createCell(row, 5, products.getRetail_price(), style);
            createCell(row, 6, products.getWhole_price(), style);
            createCell(row, 7, products.getRetail_discount(), style);
            createCell(row, 8, products.getWholesale_discount(), style);
            createCell(row, 9, products.getRetail_discount_money(), style);
            createCell(row, 10, products.getWholesale_discount_money(), style);
            createCell(row, 11, products.getQuantity(), style);
            createCell(row, 12, products.getNote(), style);
        }
    }

    private static void autosizeColumn(XSSFSheet sheet, int lastColumn) {
        for (int i = 0; i < lastColumn; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public ByteArrayInputStream export() throws IOException {
        writeHeaderLine();
        writeDataLines();
        autosizeColumn(sheet, 13);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
