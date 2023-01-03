package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Customers;
import com.nextsol.khangbb.entity.Present;
import com.nextsol.khangbb.model.SpinDTO;
import com.nextsol.khangbb.model.SpinResultDTO;

public interface SpinService {
    Customers findCustomersByPhone(String phone);
    SpinResultDTO spin (SpinDTO dto);
}
