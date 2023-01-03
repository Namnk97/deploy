package com.nextsol.khangbb.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bill_DetailDTO {

    private Long id;
    private Integer quantity;
    private String description;
    private Double discount;
    private BigDecimal discountMoney;
    private Long idProduct;
    private Long idBill;
    private String nameProduct;
    private String codeProduct;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Integer quantityReal;
    private Integer quantityPay;
    private Double discountBill;
    private BigDecimal discountMoneyBill;
    private String nameCustomer;

}
