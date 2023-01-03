package com.nextsol.khangbb.controller;


import com.nextsol.khangbb.model.*;
import com.nextsol.khangbb.service.BillDetailService;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/bill-detail")
public class BillDetailAPI {
    private final BillDetailService billDetailService;

    @GetMapping("/find-code")
    private ResponseEntity<?> findByCode(@RequestParam("codeBill") String codeBill) {
        return ResponseUtil.ok(this.billDetailService.findByBill(codeBill));
    }

    @PostMapping("/pay-product")
    private ResponseEntity<?> payProduct(@RequestBody ReturnProduct returnProduct) {
        return ResponseUtil.ok(this.billDetailService
                .payProduct(returnProduct.getCodeDTO(), returnProduct.getDetailList()));
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ReturnProduct {
    private CodeDTO codeDTO;
    private List<Bill_DetailDTO> detailList;
}
