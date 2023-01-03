package com.nextsol.khangbb.controller;


import com.nextsol.khangbb.model.PurchaseHistoryDTO;
import com.nextsol.khangbb.service.PurchaseHistoryService;
import com.nextsol.khangbb.util.ExportDebitCustomer;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/purchase-history")
public class PurchaseHistoryAPI {
    @Autowired
    private PurchaseHistoryService purchaseHistoryService;

    @GetMapping("/display-debit")
    private ResponseEntity<?> displayDebit(@RequestParam Long idCustomer) {
        return ResponseUtil.ok(purchaseHistoryService.displayDebitCustomer(idCustomer));
    }

    @GetMapping("/display")
    private ResponseEntity<?> displayRequirement(@RequestParam Long idCustomer, @RequestBody PaymentHistory pay) {
        return ResponseUtil.ok(purchaseHistoryService.displayRequirement(idCustomer, pay.getCheck()));
    }

    @PostMapping("/pay-debit")
    private ResponseEntity<?> payDebit(@RequestParam Long idCustomer, @RequestBody PaymentHistory pay) {
        return ResponseUtil.ok(purchaseHistoryService.paymentDebitCustomer(idCustomer, pay.getPayDebit()));
    }

    @GetMapping(value = "/export", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<?> downloadFileInventory(HttpServletResponse response, @RequestParam Long idCustomer) throws IOException {
        List<PurchaseHistoryDTO> dtoList = this.purchaseHistoryService.displayDebitCustomer(idCustomer);
        ExportDebitCustomer debitCustomer = new ExportDebitCustomer(new ArrayList<>(dtoList));
        ByteArrayInputStream byteArrayInputStream = debitCustomer.export();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=DSCN.xlsx");
        IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        return ResponseEntity.ok()
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @PostMapping("/pay-debit-bill")
    private ResponseEntity<?> payDebitFromBill(@RequestParam Long idCustomer, @RequestBody List<PurchaseHistoryDTO> dtoList) {
        return ResponseUtil.ok(purchaseHistoryService.paymentDebitCustomerForBill(idCustomer, dtoList));
    }


}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class PaymentHistory {
    private BigDecimal payDebit;
    private Integer check;

}

