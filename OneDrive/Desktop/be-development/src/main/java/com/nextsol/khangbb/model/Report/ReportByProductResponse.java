package com.nextsol.khangbb.model.Report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportByProductResponse {
    private Long id;
    private String typePayment;
    private Integer status;
    private String productCode;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal price;
    private String billCode;
    private Date createdDate;
    private String seller;
    private String customer;
    private BigDecimal discountProduct;
    private BigDecimal discountBill;
}
