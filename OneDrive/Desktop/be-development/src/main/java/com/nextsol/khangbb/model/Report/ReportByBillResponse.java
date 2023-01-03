package com.nextsol.khangbb.model.Report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportByBillResponse {
    private Long id;
    private String typePayment;
    private Integer status;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private String billCode;
    private Date createdDate;
    private String seller;
    private String customer;
    private BigDecimal discountProduct;
    private BigDecimal discountBill;
}
