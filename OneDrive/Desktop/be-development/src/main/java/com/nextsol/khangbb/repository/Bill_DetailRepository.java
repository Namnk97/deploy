package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Bill;
import com.nextsol.khangbb.entity.Bill_Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface Bill_DetailRepository extends JpaRepository<Bill_Detail, Long> {
    List<Bill_Detail> findByBillId(Long id);

    List<Bill_Detail> findByBill(Bill bill);
}
