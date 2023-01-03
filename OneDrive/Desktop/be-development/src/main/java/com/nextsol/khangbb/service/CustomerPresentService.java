package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.CustomerPresent;

import java.util.List;

public interface CustomerPresentService extends BaseService<CustomerPresent> {
    List<CustomerPresent> findByCustomerId (Long CustomerId);
}
