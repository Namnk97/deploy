package com.nextsol.khangbb.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductInterBranchTransferModel {
    private String name;
    private String code;
    private BigDecimal import_price;
    private BigDecimal whole_price;
    private BigDecimal retail_price;
    private Integer quantityTransfer;
    private Integer quantity;
    private BigDecimal retail_discount_money;
    private BigDecimal wholesale_discount_money;
    private String unit;
    private Date importDay;
    private String description;
    private String note;
    private Integer deleted;
    private Long idGroup;
    private Long idWarehouse;
}
