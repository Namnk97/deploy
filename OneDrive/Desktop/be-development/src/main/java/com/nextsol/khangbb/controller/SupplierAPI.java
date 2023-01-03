package com.nextsol.khangbb.controller;


import com.nextsol.khangbb.entity.Supplier;
import com.nextsol.khangbb.model.SupplierDTO;
import com.nextsol.khangbb.service.SupplierService;
import com.nextsol.khangbb.util.ExportSupplier;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/supplier")
public class SupplierAPI {


    private final SupplierService supplierService;

    @GetMapping
    private ResponseEntity<?> findAll() {
        return ResponseUtil.ok(this.supplierService.findByAll());
    }

    @GetMapping("/all")
    private ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseUtil.ok(this.supplierService.findAll(pageable));
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

    @PostMapping
    private ResponseEntity<?> create(@Valid @RequestBody SupplierDTO dto) {
        return ResponseUtil.ok(this.supplierService.create(dto));
    }

    @PutMapping
    private ResponseEntity<?> update(@Valid @RequestBody SupplierDTO dto) {
        return ResponseUtil.ok(this.supplierService.update(dto));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> delete(@PathVariable Long id) {

        return ResponseUtil.ok(this.supplierService.remove(id));
    }

    @PostMapping("/find")
    private ResponseEntity<?> findByRequirement(@RequestBody SupplierDTO dto, Pageable pageable) {
        return ResponseUtil.ok(this.supplierService.findByRequirement(dto, pageable));
    }

    @GetMapping(value = "/export", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<?> downloadFileInventory(HttpServletResponse response) throws IOException {
        List<Supplier> supplierList = this.supplierService.findALL();
        ExportSupplier exportSupplier = new ExportSupplier(new ArrayList<>(supplierList));
        ByteArrayInputStream byteArrayInputStream = exportSupplier.export();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=DSCC.xlsx");
        IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        return ResponseEntity.ok()
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<?> importFromExcelToDb(@RequestPart("file") MultipartFile files) {
        this.supplierService.importExcel(files);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
