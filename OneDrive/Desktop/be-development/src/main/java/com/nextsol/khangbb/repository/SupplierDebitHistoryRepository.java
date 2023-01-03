package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.SupplierDebitHistory;
import com.nextsol.khangbb.entity.Supplier_Debit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierDebitHistoryRepository extends JpaRepository<SupplierDebitHistory, Long> {

    List<SupplierDebitHistory> findBySupplierDebit(Supplier_Debit supplierDebit);

}
