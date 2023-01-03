package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.model.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {

    Page<ProductDTO> findRequirement(ProductDTO productDTO, Pageable pageable);

    List<ProductDTO> findByImportWarehouse(ProductDTO productDTO);
}
