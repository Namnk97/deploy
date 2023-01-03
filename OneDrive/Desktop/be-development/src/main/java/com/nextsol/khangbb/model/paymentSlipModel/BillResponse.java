package com.nextsol.khangbb.model.paymentSlipModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillResponse {
    private String productName;
    private Integer quantity;
}
