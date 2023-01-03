package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Customers_Groups;
import com.nextsol.khangbb.model.Customer_GroupDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface Customer_GroupService {

    List<Customer_GroupDTO> findByAll();

    Customer_GroupDTO create(Customer_GroupDTO dto);

    ResponseEntity<?> update(Customer_GroupDTO dto);

    Customer_GroupDTO toCustomerGroupDTO(Customers_Groups customersGroups);

    Optional<Customer_GroupDTO> findById(Long id);
    ResponseEntity<?> remove(Long id);
}
