package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.entity.Present;
import com.nextsol.khangbb.repository.PresentRepository;
import com.nextsol.khangbb.service.PresentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PresentServiceImpl implements PresentService {
    @Autowired
    private PresentRepository presentRepository;

    @Override
    public Page<Present> findAll(Pageable pageable) {
        return presentRepository.findAll(pageable);
    }

    @Override
    public Optional<Present> findById(Long id) {
        return presentRepository.findById(id);
    }

    @Override
    public Present create(Present present) {
        return null;
    }

    @Override
    public List<Present> findAll() {
        return presentRepository.findAll();
    }
}
