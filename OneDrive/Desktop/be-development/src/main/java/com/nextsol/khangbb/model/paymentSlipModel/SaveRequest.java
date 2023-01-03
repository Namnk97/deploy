package com.nextsol.khangbb.model.paymentSlipModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SaveRequest {
    private String billCode;
    @NonNull
    private String type;
    @NonNull
    private String reason;
    @NonNull
    private BigDecimal price;
    private Long customer;
    private Date orderDate;
    private Date receivedDate;
    @NonNull
    private String description;
}
