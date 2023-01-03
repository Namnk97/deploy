package com.nextsol.khangbb.service;

import com.nextsol.khangbb.model.Report.ReportBillRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity<?> getReportByBill(Pageable pageable, ReportBillRequest request);

    ResponseEntity<?> getReportByProduct(Pageable pageable, ReportBillRequest request);
}
