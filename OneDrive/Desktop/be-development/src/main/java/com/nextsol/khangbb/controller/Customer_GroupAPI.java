package com.nextsol.khangbb.controller;


import com.nextsol.khangbb.model.Customer_GroupDTO;
import com.nextsol.khangbb.service.Customer_GroupService;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/customer-group")

public class Customer_GroupAPI {


    private final Customer_GroupService customer_groupService;

    @GetMapping
    private ResponseEntity<?> findAll() {

        return ResponseUtil.ok(customer_groupService.findByAll());
    }

    @PostMapping
    private ResponseEntity<?> save(@Valid  @RequestBody Customer_GroupDTO dto) {

        return ResponseUtil.ok(this.customer_groupService.create(dto));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> delete(@PathVariable Long id) {
        this.customer_groupService.remove(id);
        return ResponseUtil.ok(HttpStatus.OK);
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
    private ResponseEntity<?> update(@Valid @RequestBody Customer_GroupDTO dto) {
        return ResponseUtil.ok(this.customer_groupService.update(dto));
    }
}
