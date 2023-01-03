package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Supplier;
import com.nextsol.khangbb.entity.Supplier_Debit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Supplier_DebitRepository extends JpaRepository<Supplier_Debit, Long> {

    List<Supplier_Debit> findBySupplierAndDeleted(Supplier supplier, Integer deleted);

    Supplier_Debit findByIdAndDeleted(Long id, Integer deleted);
}
