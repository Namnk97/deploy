package com.nextsol.khangbb.service;

import com.nextsol.khangbb.model.PurchaseHistoryDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PurchaseHistoryService {

    List<PurchaseHistoryDTO> displayDebitCustomer(Long idCustomer);

    List<PurchaseHistoryDTO> displayRequirement(Long idCustomer, Integer check);

    List<PurchaseHistoryDTO> paymentDebitCustomer(Long idCustomer, BigDecimal payDebit);

    List<PurchaseHistoryDTO> paymentDebitCustomerForBill(Long idCustomer, List<PurchaseHistoryDTO> dto);
}
