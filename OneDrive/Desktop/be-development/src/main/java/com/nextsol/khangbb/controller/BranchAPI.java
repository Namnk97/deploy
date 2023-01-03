package com.nextsol.khangbb.controller;


import com.nextsol.khangbb.entity.Products;
import com.nextsol.khangbb.model.BranchDTO;
import com.nextsol.khangbb.service.BranchService;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/branch")
public class BranchAPI {

    private final BranchService branchService;


    @GetMapping
    private ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseUtil.ok(this.branchService.findAll(pageable));
    }

    @GetMapping("/find-all")
    private ResponseEntity<?> findByDeleted() {
        return ResponseUtil.ok(this.branchService.findByDeleted());
    }

    @PostMapping
    private ResponseEntity<?> save(@Valid @RequestBody BranchDTO branchDTO) {
        return ResponseUtil.ok(this.branchService.create(branchDTO));
    }

    @PutMapping
    private ResponseEntity<?> update(@Valid @RequestBody BranchDTO branchDTO) {
        return ResponseUtil.ok(this.branchService.update(branchDTO));
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

    @DeleteMapping
    private ResponseEntity<?> delete(@RequestBody BranchDTO dto) {
        return this.branchService.remove(dto.getId());
    }

    @PostMapping("/importShopProduct")
    private ResponseEntity<?> importExcelShopProduct(@RequestBody MultipartFile file) throws IOException {
        return ResponseEntity.ok(this.branchService.importExcelShopProduct(file));
    }

    @PostMapping("/importInventory")
    private ResponseEntity<?> importExcelInventory(@RequestParam("file") MultipartFile file) throws IOException {
        return this.branchService.importExcelInventory(file);
    }

    @PostMapping("/importInterBranchTransfer")
    private ResponseEntity<?> importInterBranchTransfer(@RequestParam("file") MultipartFile file) throws IOException {
        return this.branchService.importInterBranchTransfer(file);
    }

    @PostMapping("/saveList")
    private ResponseEntity<?> saveList(List<Products> products) {
        this.branchService.saveList(products);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
