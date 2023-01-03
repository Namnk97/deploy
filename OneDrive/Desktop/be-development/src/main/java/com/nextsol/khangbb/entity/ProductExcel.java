package com.nextsol.khangbb.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_product")
public class ProductExcel extends BaseEntity {

    private String name;

    private String code;

    private BigDecimal importPrice;

    private BigDecimal whole_price;

    private BigDecimal retail_price;

    private Integer quantity;

    private String unit;

    private String description;

    private String note;

    private Integer deleted;

    private BigDecimal retail_discount_money;
    private BigDecimal wholesale_discount_money;
    private Double retail_discount;
    private Double wholesale_discount;
    private Double discount;
    private BigDecimal discountMoney;
    private Long branch_id;
    private Long group_id;
}
