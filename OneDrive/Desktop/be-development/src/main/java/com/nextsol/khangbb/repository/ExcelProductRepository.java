package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.ProductExcel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ExcelProductRepository extends JpaRepository<ProductExcel, Long> {

    List<ProductExcel> findByDeleted(Integer deleted);
}
