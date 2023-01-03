package com.nextsol.khangbb.model.paymentSlipModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBillResponse {
    private Long id;
    private String name;
    private Integer quantity;
    private BigDecimal importPrice;
    private BigDecimal whole_price;
    private BigDecimal retail_price;
    private BigDecimal retail_discount_money;
    private BigDecimal wholesale_discount_money;
    private Double retail_discount;
    private Double wholesale_discount;
    private Double discount;
    private BigDecimal discountMoney;
    private String description;
}
