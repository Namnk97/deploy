package com.nextsol.khangbb.util;

import com.nextsol.khangbb.entity.Products;
import com.nextsol.khangbb.repository.ProductsRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class ExportInventory {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Products> productsList;

    @Autowired
    private ProductsRepository productsRepository;

    public ExportInventory(List<Products> productsList) {
        this.productsList = productsList;
        workbook = new XSSFWorkbook();

    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("DSKK");

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
        createCell(row, 3, "Giá nhập", style);
        createCell(row, 4, "Số lượng hiện tại", style);
        createCell(row, 5, "Số lượng thực tế", style);
        createCell(row, 6, "Ghi chú", style);
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
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Products products : productsList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, products.getCode(), style);
            createCell(row, columnCount++, products.getName(), style);
            createCell(row, columnCount++, products.getUnit(), style);
            createCell(row, columnCount++, products.getImportPrice(), style);
            createCell(row, columnCount++, "", style);
            createCell(row, columnCount++, products.getQuantity(), style);
            createCell(row, columnCount++, products.getNote(), style);

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
