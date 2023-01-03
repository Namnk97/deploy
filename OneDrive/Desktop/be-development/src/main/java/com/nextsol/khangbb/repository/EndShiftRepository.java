package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.EndShift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndShiftRepository extends JpaRepository<EndShift, Long> {
    EndShift findFirstByOrderByEndDateDesc();
}
