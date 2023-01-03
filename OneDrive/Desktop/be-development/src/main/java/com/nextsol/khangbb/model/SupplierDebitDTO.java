package com.nextsol.khangbb.model;


import com.nextsol.khangbb.entity.Supplier;
import com.nextsol.khangbb.entity.SupplierDebitHistory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDebitDTO extends BaseModel {
    private Long id;
    private BigDecimal debit;
    private BigDecimal pay_debit;
    private Long idSupplier;
    private Integer deleted;
    private String code;
    private Supplier supplier;
    private List<SupplierDebitHistory> supplierDebitHistories;

}
