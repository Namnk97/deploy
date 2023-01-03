package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Long> {
    Optional<Bonus> findByBonusCodeAndActivated (String bonusCode, Integer activated);
    Optional<Bonus> findByBonusCode(String bonusCode);
}
