package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Roles;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleService extends BaseService<Roles>{
    List<Roles> getAll();
}
