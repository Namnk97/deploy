package com.nextsol.khangbb.controller;


import com.nextsol.khangbb.model.Supplier_GroupDTO;
import com.nextsol.khangbb.service.Supplier_GroupService;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/supplier-group")
public class Supplier_GroupAPI {

    private final Supplier_GroupService supplier_groupService;

    @GetMapping
    private ResponseEntity<?> findAll() {

        return ResponseEntity.ok(supplier_groupService.findByAll());
    }

    @PostMapping
    private ResponseEntity<?> create(@Valid @RequestBody Supplier_GroupDTO dto) {
        return ResponseUtil.ok(this.supplier_groupService.create(dto));
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @PutMapping
    private ResponseEntity<?> update(@Valid @RequestBody Supplier_GroupDTO dto) {
        return ResponseUtil.ok(this.supplier_groupService.update(dto));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> delete(@PathVariable Long id) {

        return ResponseUtil.ok(this.supplier_groupService.remove(id));
    }


}
