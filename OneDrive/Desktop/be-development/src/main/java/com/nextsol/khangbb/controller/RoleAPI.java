package com.nextsol.khangbb.controller;

import com.nextsol.khangbb.constant.Constant;
import com.nextsol.khangbb.entity.Roles;
import com.nextsol.khangbb.service.RoleService;
import com.nextsol.khangbb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/role")
public class RoleAPI {

    @Autowired
    private RoleService roleService;

    @GetMapping
    private ResponseEntity<?> getAll(){
        List<Roles> roles = roleService.getAll();
        roles.remove(roles.stream().filter(x -> x.getId().equals(Constant.SUPER_ADMIN_ID)).findAny().orElse(null));
        return ResponseUtil.ok(roles);
    }
}
