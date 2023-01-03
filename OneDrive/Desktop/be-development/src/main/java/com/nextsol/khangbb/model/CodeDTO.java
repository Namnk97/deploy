package com.nextsol.khangbb.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CodeDTO extends BaseModel {

    private String code;
    private String type;
    private String note;
    private Long branchFrom;
    private Long branchTo;
    private Long idSupplier;
    private String codeBill;
    private Integer status;//1 chấp nhận 2 từ chối 3 chờ xác nhận
    private String reason;
    private Long moneyPayment; // Số tiền NCC thanh toán
    private String action;

}
