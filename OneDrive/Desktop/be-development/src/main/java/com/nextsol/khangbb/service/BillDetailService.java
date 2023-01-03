package com.nextsol.khangbb.service;

import com.nextsol.khangbb.model.Bill_DetailDTO;
import com.nextsol.khangbb.model.CodeDTO;

import java.util.List;

public interface BillDetailService {

    List<Bill_DetailDTO> findByBill(String codeBill);

    List<Bill_DetailDTO> payProduct(CodeDTO codeDTO, List<Bill_DetailDTO> detailList);
}
