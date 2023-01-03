package com.nextsol.khangbb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BaseService<T> {
    Page<T> findAll(Pageable pageable);


    Optional<T> findById(Long id);

    T create(T t);

}
