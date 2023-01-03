package com.nextsol.khangbb.model;


import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PurchaseHistoryDTO extends BaseModel{
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
    private String card;
    private Date createdDate;
    private String createdBy;
    private String seller;
    private Long idBill;
    private String nameCustomer;
}
