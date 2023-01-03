package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Product_GroupExcel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductGroupExcelRepository extends JpaRepository<Product_GroupExcel, Long> {

    List<Product_GroupExcel> findByDeleted(Integer deleted);
}
