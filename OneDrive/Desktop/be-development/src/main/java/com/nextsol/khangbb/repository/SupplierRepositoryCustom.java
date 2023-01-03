package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.model.SupplierDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SupplierRepositoryCustom {

    Page<SupplierDTO> findByRequirement(SupplierDTO dto, Pageable pageable);
}
