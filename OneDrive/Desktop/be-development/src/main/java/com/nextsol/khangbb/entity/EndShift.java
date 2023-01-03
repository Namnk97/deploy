package com.nextsol.khangbb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_end_shift")
public class EndShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date startDate;
    private Date endDate;
    private Double beforePrice;
    private Double afterPrice;
    private BigDecimal cash;
    private BigDecimal transfer;
    private BigDecimal card;
    private BigDecimal VNPay;
    private BigDecimal debit;
}
