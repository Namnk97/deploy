package com.nextsol.khangbb.repository;


import com.nextsol.khangbb.model.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryCustom {

    Page<CustomerDTO> findRequirementCustomer(CustomerDTO dto, Pageable pageable);
}
