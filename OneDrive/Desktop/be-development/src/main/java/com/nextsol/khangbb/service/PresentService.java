package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Present;

import java.util.List;

public interface PresentService extends BaseService<Present> {
    List<Present> findAll();
}
