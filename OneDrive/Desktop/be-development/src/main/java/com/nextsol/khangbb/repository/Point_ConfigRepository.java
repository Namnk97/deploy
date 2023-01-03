package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Point_Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Point_ConfigRepository extends JpaRepository<Point_Config,Long> {
}
