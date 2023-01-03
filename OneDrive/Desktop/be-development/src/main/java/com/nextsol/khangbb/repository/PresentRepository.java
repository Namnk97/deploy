package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Present;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentRepository extends JpaRepository<Present, Long> {
}
