package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Bill findByCode(String code);

    @Query("SELECT b.id FROM Bill b " +
            " WHERE b.code LIKE %:code% ORDER BY b.id DESC")
    List<Long> findFirstIdByType(@Param("code") String code);


    List<Bill> findByCodeContaining(String code);



}
