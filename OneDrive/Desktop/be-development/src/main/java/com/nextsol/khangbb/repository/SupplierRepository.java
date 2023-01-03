package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, SupplierRepositoryCustom {

    Page<Supplier> findByDeleted(Integer deleted, Pageable pageable);

    List<Supplier> findByDeleted(Integer deleted);

    Supplier findByPhone(String phone);

    Supplier findByName(String name);

    Supplier findByNameAndDeleted(String name, Integer deleted);

}
