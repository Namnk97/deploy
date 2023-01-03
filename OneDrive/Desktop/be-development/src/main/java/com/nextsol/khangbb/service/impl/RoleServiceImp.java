package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.entity.Roles;
import com.nextsol.khangbb.repository.RolesRepository;
import com.nextsol.khangbb.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImp implements RoleService {
    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public List<Roles> getAll() {
        return rolesRepository.findAll();
    }

    @Override
    public Page<Roles> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Roles> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Roles create(Roles roles) {
        return null;
    }
}
