package com.nextsol.khangbb.controller;


import com.nextsol.khangbb.model.SupplierDebitDTO;
import com.nextsol.khangbb.service.Supplier_DebitService;
import com.nextsol.khangbb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/supplier-debit")
public class SupplierDebitAPI {
    @Autowired
    private Supplier_DebitService supplier_debitService;

    @GetMapping("/{id}")
    private ResponseEntity<?> displaySupplierDebit(@PathVariable Long id) {
        return ResponseUtil.ok(this.supplier_debitService.findByDebit(id));
    }

    @PostMapping("/payment-debit")
    private ResponseEntity<?> paymentDebit(@RequestBody SupplierDebitDTO supplierDebitDTO) {
        return this.supplier_debitService.paymentDebit(supplierDebitDTO);
    }

    @PostMapping(value ="/import-excel-debit", consumes = "multipart/form-data")
    private ResponseEntity<?> importExcelDebit(@RequestPart("file") MultipartFile files) {
        return supplier_debitService.importExcelDebit(files);
    }
}
