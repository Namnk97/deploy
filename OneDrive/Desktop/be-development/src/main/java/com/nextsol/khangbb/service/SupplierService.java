package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Supplier;
import com.nextsol.khangbb.model.SupplierDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SupplierService extends BaseService<SupplierDTO> {

    List<SupplierDTO> findByAll();

    List<Supplier> findALL();

    SupplierDTO update(SupplierDTO dto);

    SupplierDTO toSupplierDTO(Supplier group);

    ResponseEntity<?> remove(Long id);

    Page<SupplierDTO> findByRequirement(SupplierDTO dto, Pageable pageable);

    void importExcel(MultipartFile multipartfiles);

}
