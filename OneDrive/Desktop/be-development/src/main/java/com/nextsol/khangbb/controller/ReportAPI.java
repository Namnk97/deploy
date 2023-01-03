package com.nextsol.khangbb.controller;

import com.nextsol.khangbb.model.Report.ReportBillRequest;
import com.nextsol.khangbb.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
public class ReportAPI {

    private final ReportService reportService;

    @GetMapping("getReportByBill")
    private ResponseEntity<?> getReportByBill(Pageable pageable, @RequestBody ReportBillRequest request) {
        return reportService.getReportByBill(pageable,request);
    }

    @GetMapping("getReportByProduct")
    private ResponseEntity<?> getReportByProduct(Pageable pageable, @RequestBody ReportBillRequest request) {
        return reportService.getReportByProduct(pageable,request);
    }

}
