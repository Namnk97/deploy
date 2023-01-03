package com.nextsol.khangbb.model.paymentSlipModel;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PurchaseResponse {
    //typePayment,point,type,reason,status,price,totalPrice,orderDate,receivedDate,description,code,createdDate,createdBy
    private Long id;
    private String typePayment;
    private Integer point;
    private String type;
    private String reason;
    private Integer status;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Date orderDate;
    private Date receivedDate;
    private String description;
    private String billCode;
    private Date createdDate;
    private String createdBy;
    private String seller;
    private Long customer;
    private Long endShift;
}
