package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Customers;
import com.nextsol.khangbb.model.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CustomerService extends BaseService<CustomerDTO> {

    CustomerDTO toCustomerDTO(Customers customer);

    ResponseEntity<?> remove(Long id);

    CustomerDTO update(CustomerDTO dto);

    Page<CustomerDTO> findByRequirement(CustomerDTO dto, Pageable pageable);

    Customers findByPhone(String phone);

    CustomerDTO findByID(Long id);

    List<Customers> findByDeleted();

    ResponseEntity<?> findByNamePhone(String request);

    void importExcel(MultipartFile multipartfiles);

    Customers save(CustomerDTO customerDTO);

    List<CustomerDTO> findByNameOrPhone(String search);

    List<CustomerDTO> findByCustomerGroup(List<Long> idCustomer, Long idGroup);


}
