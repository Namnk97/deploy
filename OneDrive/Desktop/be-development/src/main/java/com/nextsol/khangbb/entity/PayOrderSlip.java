package com.nextsol.khangbb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_pay_order_slip")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderSlip {
    @Id
    private String id;
    private String orderBillCode;
}
