package com.nextsol.khangbb.model.paymentSlipModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndShiftResponse {
    private Long id;
    private Date startDate;
    private Date endDate;
    private String user;
    private Double beforePrice;
    private Double afterPrice;
}
