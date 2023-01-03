package com.nextsol.khangbb.model.paymentSlipModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BillDTO {
    private PurchaseResponse bill;
    private String customerName;
    private Long customerId;
    private Long orderBillId;
    private List<ProductBillResponse> productList;
}
