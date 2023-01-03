package com.nextsol.khangbb.model.paymentSlipModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {
    private Long id;
    @NonNull
    private BigDecimal price;
    private Long customer;
    private String seller;
    private Date orderDate;
    private Date receivedDate;
    private String description;
}
