package com.nextsol.khangbb.model.Report;

import lombok.Data;

import java.util.Date;

@Data
public class ReportBillRequest {
    private Date fromDate;
    private Date toDate;
    private String billCode;
    private String customer;
    private String seller;
    private String productId;
    private String productName;
}
