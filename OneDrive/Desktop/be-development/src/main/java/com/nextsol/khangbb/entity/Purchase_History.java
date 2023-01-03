package com.nextsol.khangbb.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "tbl_purchase_history")
public class Purchase_History extends BaseEntity {
    private String typePayment;
    private Integer point;
    private String type;
    private String seller;
    private String reason;
    private Integer status = 0;
    private Long endShift;
    private Integer endLastShift = 0;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private Date orderDate;
    private Date receivedDate;
    private String description;
    private Long branchId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    @ToString.Exclude
    private Customers customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    @JsonIgnore
    @ToString.Exclude
    private Bill bill;


}
