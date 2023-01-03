package com.nextsol.khangbb.service;

import com.nextsol.khangbb.model.SupplierDebitDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface Supplier_DebitService {

    List<SupplierDebitDTO> findByDebit(Long idSupplier);

    ResponseEntity<?> paymentDebit(SupplierDebitDTO supplierDebitDTO);

    ResponseEntity<?> importExcelDebit(MultipartFile multipartFile);
}
