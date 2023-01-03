package com.nextsol.khangbb.model.paymentSlipModel;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Data
@ToString
public class FilterDTO {
    Page<PurchaseResponse> purchaseResponses;
    private BigDecimal totalCollection;
    private BigDecimal totalSpending;
    private BigDecimal totalPrice;
}
