package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.CustomerPresent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CustomerPresentRepository extends JpaRepository<CustomerPresent, Long> {
    List<CustomerPresent> findAllByCustomerId(Long id);
}
