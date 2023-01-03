package com.nextsol.khangbb.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;


@Data
@NoArgsConstructor
public class ReportImportBranch {

    private String codeProduct;
    private String nameProduct;
    private Integer quantity;
    private BigDecimal importPrice;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDay;

    @JsonProperty("dayFrom")
    private Date dayFrom;

    @JsonProperty("dayTo")
    private Date dayTo;
    private String codeCode;
    private String typeCode;
    private String note;

    public ReportImportBranch(String codeProduct, String nameProduct, Integer quantity, BigDecimal importPrice, Date createDay, String codeCode, String typeCode, String note) {
        this.codeProduct = codeProduct;
        this.nameProduct = nameProduct;
        this.quantity = quantity;
        this.importPrice = importPrice;
        this.createDay = createDay;
        this.codeCode = codeCode;
        this.typeCode = typeCode;
        this.note = note;
    }


}
