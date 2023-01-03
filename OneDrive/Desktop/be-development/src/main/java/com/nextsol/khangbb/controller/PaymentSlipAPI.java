package com.nextsol.khangbb.controller;

import com.nextsol.khangbb.entity.Purchase_History;
import com.nextsol.khangbb.entity.Users;
import com.nextsol.khangbb.model.paymentSlipModel.EndShiftFilterRequest;
import com.nextsol.khangbb.model.paymentSlipModel.FilterRequest;
import com.nextsol.khangbb.model.paymentSlipModel.SaveRequest;
import com.nextsol.khangbb.model.paymentSlipModel.UpdateRequest;
import com.nextsol.khangbb.service.PaymentSlipService;
import com.nextsol.khangbb.util.ExportPaymentSlip;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/payment-slip")
public class PaymentSlipAPI {

    @Autowired
    private PaymentSlipService paymentSlipService;

    @PostMapping("filter")
    private ResponseEntity<?> getAll(Pageable pageable, @RequestBody FilterRequest filterRequest) {
        return paymentSlipService.findAllDTO(pageable, filterRequest);
    }

    @GetMapping("getListOrderBill")
    private ResponseEntity<?> getListOrderBill(){
        return paymentSlipService.getListOrderBill();
    }

    @PostMapping
    private ResponseEntity<?> save(@RequestBody SaveRequest request) {
        return paymentSlipService.save(request);
    }

    @PutMapping
    private ResponseEntity<?> update(@RequestBody UpdateRequest request) {
        return paymentSlipService.update(request);
    }

    @DeleteMapping
    private ResponseEntity<?> remove(@RequestParam Long id) {
        return paymentSlipService.remove(id);
    }

    @GetMapping("statisticalEndShift")
    private ResponseEntity<?> statisticalEndShift() {
        return paymentSlipService.statisticalEndShift();
    }

    @PostMapping("endShift")
    private ResponseEntity<?> endShift(@RequestParam Integer accept, @RequestParam BigDecimal price) {
        return paymentSlipService.endShift(accept, price);
    }

    @PostMapping("endShiftFilter")
    private ResponseEntity<?> endShiftFilter(Pageable pageable, @RequestBody EndShiftFilterRequest request) {
        return paymentSlipService.endShiftFilter(request, pageable);
    }

    @GetMapping("getEndShiftList")
    private ResponseEntity<?> getEndShiftList(Pageable pageable) {
        return paymentSlipService.getEndShiftList(pageable);
    }

    @GetMapping("endShiftDetail")
    private ResponseEntity<?> endShiftDetail(Pageable pageable, @RequestParam Long id) {
        return paymentSlipService.endShiftDetail(id, pageable);
    }

    @PostMapping("adjustSafes")
    private ResponseEntity<?> adjustSafes(@RequestParam BigDecimal editPrice) {
        return paymentSlipService.adjustSafes(editPrice);
    }

    @PostMapping("editBillDetail")
    private ResponseEntity<?> editBillDetail(@RequestBody UpdateRequest request) {
        return paymentSlipService.editBillDetail(request);
    }

    @GetMapping("printfBill")
    private ResponseEntity<?> exportPDF(@RequestParam String code) {
        return paymentSlipService.printfBill(code);
    }

    @PostMapping("export")
    public ResponseEntity<?> export(Pageable pageable, @RequestBody FilterRequest filterRequest, HttpServletResponse response) throws IOException {
        List<Purchase_History> purchase_histories = paymentSlipService.findAll(pageable, filterRequest).toList();
        Users user = paymentSlipService.getInfoUser();
        ExportPaymentSlip exportExcel = new ExportPaymentSlip(purchase_histories, user);
        ByteArrayInputStream byteArrayInputStream = exportExcel.export(filterRequest);
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("hh_mm_ss_dd_MM_yyyy");
        String currentDateTime = dateFormatter.format(new Date());
        response.setHeader("Content-Disposition", "attachment; filename=BaoCaoThuChi_" + currentDateTime + ".xls");
        IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        return ResponseEntity.ok()
                .body(new InputStreamResource(byteArrayInputStream));
    }
}
