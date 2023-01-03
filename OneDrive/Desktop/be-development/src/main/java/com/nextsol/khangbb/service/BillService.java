package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Bill;
import com.nextsol.khangbb.model.Bill_BillDTO;
import com.nextsol.khangbb.model.ProductDTO;
import com.nextsol.khangbb.model.PurchaseHistoryDTO;
import com.nextsol.khangbb.model.ReportImportBranch;
import com.nextsol.khangbb.model.paymentSlipModel.BillDTO;
import com.nextsol.khangbb.model.paymentSlipModel.PurchaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface BillService {
    Page<Bill> getAll(Pageable pageable);

    BillDTO detail(Long id);

    Bill_BillDTO payMoney(Bill_BillDTO billDTO, List<ProductDTO> dto, PurchaseHistoryDTO purchase, String phone);

    List<BillDTO> codeBill(String codeBill);




}
