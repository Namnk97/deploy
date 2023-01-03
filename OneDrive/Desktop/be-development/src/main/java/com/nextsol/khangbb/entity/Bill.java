package com.nextsol.khangbb.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_bill")
public class Bill extends BaseEntity {

    private String code;
    private Double discount;
    private BigDecimal discountMoney;


    @OneToMany(mappedBy = "bill", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<Bill_Detail> billDetail;

    @OneToMany(mappedBy = "bill")
    @ToString.Exclude
    private List<Purchase_History> purchaseHistoryList;
}
