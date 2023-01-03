package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.constant.Constant;
import com.nextsol.khangbb.entity.Users;
import com.nextsol.khangbb.model.Report.ReportBillRequest;
import com.nextsol.khangbb.model.Report.ReportByBillResponse;
import com.nextsol.khangbb.model.Report.ReportByProductResponse;
import com.nextsol.khangbb.repository.Purchase_HistoryRepository;
import com.nextsol.khangbb.repository.UserRepository;
import com.nextsol.khangbb.service.ReportService;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final Purchase_HistoryRepository purchase_historyRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> getReportByBill(Pageable pageable, ReportBillRequest request) {
        List<String> listReason = com.nextsol.khangbb.constant.Constant.REPORT_BILL_CODE;
        Page<ReportByBillResponse> list = purchase_historyRepository.getReportByBill(
                pageable,
                request.getFromDate(),
                request.getToDate(),
                request.getBillCode(),
                request.getCustomer(),
                request.getSeller(),
                request.getProductId(),
                request.getProductName(),
                getInfoUser().getBranch().getId(),
                listReason
        );
        BigDecimal cash = BigDecimal.valueOf(0);
        BigDecimal transfer = BigDecimal.valueOf(0);
        BigDecimal VNPay = BigDecimal.valueOf(0);
        BigDecimal card = BigDecimal.valueOf(0);
        BigDecimal debit = BigDecimal.valueOf(0);
        BigDecimal discountProduct = BigDecimal.valueOf(0);
        BigDecimal discountBill = BigDecimal.valueOf(0);
        for (ReportByBillResponse item : list) {
            discountBill = discountProduct.add(item.getDiscountBill() == null ? BigDecimal.valueOf(0) : item.getDiscountBill());
            discountProduct = discountProduct.add(item.getDiscountProduct() == null ? BigDecimal.valueOf(0) : item.getDiscountProduct());
            if (item.getStatus().equals(2))
                continue;
            if (item.getTypePayment() == null || item.getPrice() == null)
                continue;
            if (item.getTypePayment().equals(Constant.CASH_PAYMENT_TYPE))
                cash = cash.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.TRANSFER_PAYMENT_TYPE))
                transfer = transfer.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.VNPAY_PAYMENT_TYPE))
                VNPay = VNPay.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.CARD_PAYMENT_TYPE))
                card = card.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.DEBIT_PAYMENT_TYPE))
                debit = debit.add(item.getPrice());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("cash", cash);
        response.put("transfer", transfer);
        response.put("VNPay", VNPay);
        response.put("card", card);
        response.put("debit", debit);
        response.put("discountBill", discountBill);
        response.put("discountProduct", discountProduct);
        response.put("reportList", list);
        return ResponseUtil.ok(response);
    }

    @Override
    public ResponseEntity<?> getReportByProduct(Pageable pageable, ReportBillRequest request){
        List<String> listReason = com.nextsol.khangbb.constant.Constant.REPORT_BILL_CODE;
        Page<ReportByProductResponse> list = purchase_historyRepository.getReportByProduct(
                pageable,
                request.getFromDate(),
                request.getToDate(),
                request.getBillCode(),
                request.getCustomer(),
                request.getSeller(),
                request.getProductId(),
                request.getProductName(),
                getInfoUser().getBranch().getId(),
                listReason
        );
        BigDecimal priceSell = BigDecimal.valueOf(0);
        BigDecimal priceRepay = BigDecimal.valueOf(0);
        int quantitySell = 0;
        int quantityRepay = 0;
        for (ReportByProductResponse item : list) {
            if (item.getStatus().equals(2))
                continue;
            if (item.getBillCode().contains("CDH")) {
                priceRepay = priceRepay.add(item.getDiscountBill() == null ? BigDecimal.valueOf(0) : item.getDiscountBill());
                quantityRepay += item.getQuantity() == null ? 0 : item.getQuantity();
            }else {
                priceSell = priceSell.add(item.getDiscountProduct() == null ? BigDecimal.valueOf(0) : item.getDiscountProduct());
                quantitySell += item.getQuantity() == null ? 0 : item.getQuantity();
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("priceSell", priceSell);
        response.put("priceRepay", priceRepay);
        response.put("quantitySell", quantitySell);
        response.put("quantityRepay", quantityRepay);
        response.put("reportList", list);
        return ResponseUtil.ok(response);
    }

    public Users getInfoUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(null);
    }
}
