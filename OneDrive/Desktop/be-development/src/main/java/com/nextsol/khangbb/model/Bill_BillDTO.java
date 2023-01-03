package com.nextsol.khangbb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bill_BillDTO extends BaseModel {

    private String code;
    private Double discount;
    private BigDecimal discountMoney;
}
