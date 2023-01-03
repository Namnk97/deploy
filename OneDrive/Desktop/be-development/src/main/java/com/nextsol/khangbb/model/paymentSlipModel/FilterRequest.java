package com.nextsol.khangbb.model.paymentSlipModel;

import lombok.Data;
import lombok.Getter;

import java.util.Date;
@Data
public class FilterRequest {
    private Date fromDate;
    private Date toDate;
    private String type;
    private String reason;
    private String billCode;
    private Long customer;
}
