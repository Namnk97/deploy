package com.nextsol.khangbb.controller;

import com.nextsol.khangbb.model.SpinDTO;
import com.nextsol.khangbb.service.SpinService;
import com.nextsol.khangbb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/spin")
public class SpinApi {
    @Autowired
    private SpinService service;

    @PostMapping("/phone")
    private ResponseEntity<?> checkPhone (@RequestParam String phone){
        return ResponseEntity.ok(service.findCustomersByPhone(phone));
    }

    @PostMapping("")
    private ResponseEntity<?> spin  (@RequestBody SpinDTO dto) {
        return ResponseUtil.ok(service.spin(dto));
    }
}
