package com.nextsol.khangbb.model.paymentSlipModel;

import lombok.Data;

import java.util.Date;

@Data
public class EndShiftFilterRequest {
    private String user;
    private Date fromDate;
    private Date toDate;
}
