package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.entity.CustomerPresent;
import com.nextsol.khangbb.repository.CustomerPresentRepository;
import com.nextsol.khangbb.service.CustomerPresentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerPresentServiceImpl implements CustomerPresentService {
    @Autowired
    private CustomerPresentRepository repository;

    @Override
    public Page<CustomerPresent> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Optional<CustomerPresent> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public CustomerPresent create(CustomerPresent customerPresent) {
        return repository.save(customerPresent);
    }

    @Override
    public List<CustomerPresent> findByCustomerId(Long customerId) {
        return repository.findAllByCustomerId(customerId);
    }
}
