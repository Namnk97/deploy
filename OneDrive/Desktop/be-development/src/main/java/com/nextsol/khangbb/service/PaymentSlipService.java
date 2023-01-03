package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Purchase_History;
import com.nextsol.khangbb.entity.Users;
import com.nextsol.khangbb.model.paymentSlipModel.EndShiftFilterRequest;
import com.nextsol.khangbb.model.paymentSlipModel.FilterRequest;
import com.nextsol.khangbb.model.paymentSlipModel.SaveRequest;
import com.nextsol.khangbb.model.paymentSlipModel.UpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface PaymentSlipService {
    Page<Purchase_History> findAll(Pageable pageable, FilterRequest filterRequest);
    ResponseEntity<?> findAllDTO(Pageable pageable, FilterRequest filterRequest);

    ResponseEntity<?> getListOrderBill();

    ResponseEntity<?> save(SaveRequest request);
    ResponseEntity<?> update(UpdateRequest request);

    ResponseEntity<?> remove(Long id);

    ResponseEntity<?> statisticalEndShift();

    ResponseEntity<?> endShift(Integer accept, BigDecimal price);

    ResponseEntity<?> endShiftFilter(EndShiftFilterRequest request, Pageable pageable);

    ResponseEntity<?> endShiftDetail(Long id, Pageable pageable);
    ResponseEntity<?> getEndShiftList(Pageable pageable);

    ResponseEntity<?> adjustSafes(BigDecimal editPrice);

    ResponseEntity<?> editBillDetail(UpdateRequest request);

    ResponseEntity<?> printfBill(String code);

    Users getInfoUser();
}
