package com.nextsol.khangbb.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductInventoryDTO {
    private String code;
    private String name;
    private String unit;
    private BigDecimal importPrice;
    private Integer quantity;
    private Integer quantityFrom;
    private String description;

}
