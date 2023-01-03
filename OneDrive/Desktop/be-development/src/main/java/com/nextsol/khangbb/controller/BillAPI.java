package com.nextsol.khangbb.controller;

import com.nextsol.khangbb.model.Bill_BillDTO;
import com.nextsol.khangbb.model.ProductDTO;
import com.nextsol.khangbb.model.PurchaseHistoryDTO;
import com.nextsol.khangbb.model.paymentSlipModel.PurchaseResponse;
import com.nextsol.khangbb.service.BillService;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/bill")
public class BillAPI {

    @Autowired
    private BillService billService;

    @GetMapping
    private ResponseEntity<?> getAll(Pageable pageable) {
        return ResponseUtil.ok(billService.getAll(pageable));
    }

    @GetMapping("detail")
    private ResponseEntity<?> detail(@RequestParam Long id) {
        return ResponseUtil.ok(billService.detail(id));
    }

    @PostMapping
    private ResponseEntity<?> paymentProduct(@RequestBody PayProduct payProduct) {
        return ResponseUtil.ok(this.billService.payMoney(payProduct.getBillDTO(), payProduct.getDto(), payProduct.getResponse(), payProduct.getPhone()));
    }

    @GetMapping("/find-code")
    private ResponseEntity<?> getCodeBill(@RequestParam String codeBill) {

        return ResponseUtil.ok(this.billService.codeBill(codeBill));
    }


}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class PayProduct {
    private Bill_BillDTO billDTO;
    private List<ProductDTO> dto;
    private PurchaseHistoryDTO response;
    private String phone;
}
