package com.nextsol.khangbb.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.nextsol.khangbb.entity.Branch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExportBill {
    public ByteArrayOutputStream exportData() throws DocumentException, IOException {
        Branch branch = new Branch();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, baos);
//        Rectangle one = new Rectangle(500,140);
//        document.setPageSize(one);
//        document.setMargins(2, 2, 2, 2);
        document.open();
        PdfContentByte cb = pdfWriter.getDirectContent();
        BaseFont baseFont = BaseFont.createFont("ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font title = new Font(baseFont, 18);
        baseFont = BaseFont.createFont("ARIALUNI.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font content = new Font(baseFont, 12);
        BarcodeEAN codeEAN = new BarcodeEAN();
        codeEAN.setCodeType(codeEAN.EAN13);
        codeEAN.setCode("9780201615883");
        Image imageEAN = codeEAN.createImageWithBarcode(cb, null, null);
        float x = (PageSize.A4.getWidth() - imageEAN.getScaledWidth()) / 2;
        float y = 5;
        imageEAN.setAbsolutePosition(x, y);
        document.add(imageEAN);

        printfDoc("Shop KHANG BABY",title,true,document,10);
        Paragraph paragraph = new Paragraph("Shop KHANG BABY",title);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(10);
        document.add(paragraph);

        paragraph = new Paragraph("Đ/c: "+branch.getAddress(),content);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(5);
        document.add(paragraph);

        paragraph = new Paragraph("ĐT: "+branch.getPhone(),content);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(5);
        document.add(paragraph);

        paragraph = new Paragraph("HÓA ĐƠN BÁN HÀNG",title);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(5);
        document.add(paragraph);

        paragraph = new Paragraph("Số HĐ: ",content);
        document.add(paragraph);

        paragraph = new Paragraph("Thời gian: ",content);
        document.add(paragraph);

        paragraph = new Paragraph("Thu ngân: ",content);
        document.add(paragraph);

        String[] listTitle = {"Sản phẩm", "SL", "Đơn giá", "T. Tiền"};
        PdfPTable table = new PdfPTable(listTitle.length);
        for (String item : listTitle) {
            table.addCell(new PdfPCell(new Paragraph(item,content)));
        }

        paragraph = new Paragraph("Cám ơn quý khách!",content);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(5);
        document.add(paragraph);
        document.close();
        pdfWriter.close();
        baos.close();
        return baos;
    }

    private void printfDoc(String text, Font font, Boolean center, Document document, int space) throws DocumentException {
        Paragraph paragraph = new Paragraph(text,font);
        if (Boolean.TRUE.equals(center))
            paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingAfter(space);
        document.add(paragraph);
    }
}

