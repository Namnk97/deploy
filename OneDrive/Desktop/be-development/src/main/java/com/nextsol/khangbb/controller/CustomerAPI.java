package com.nextsol.khangbb.controller;


import com.nextsol.khangbb.entity.Customers;
import com.nextsol.khangbb.model.CustomerDTO;
import com.nextsol.khangbb.service.CustomerService;
import com.nextsol.khangbb.util.ExportCustomer;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/customer")
public class CustomerAPI {


    private final CustomerService customerService;

    @GetMapping
    private ResponseEntity<?> findAll(Pageable pageable) {

        return ResponseUtil.ok(this.customerService.findAll(pageable));
    }

    @PostMapping
    private ResponseEntity<?> save(@Valid @RequestBody CustomerDTO dto) {
        return ResponseUtil.ok(this.customerService.create(dto));
    }

    @PutMapping
    private ResponseEntity<?> update(@Valid  @RequestBody CustomerDTO dto) {
        return ResponseUtil.ok(this.customerService.update(dto));
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

    @DeleteMapping("/{id}")
    private ResponseEntity<?> delete(@PathVariable Long id) {
        this.customerService.remove(id);
        return ResponseUtil.ok(HttpStatus.OK);
    }

    @PostMapping("/find-requirement")
    private ResponseEntity<?> findByRequirement(@RequestBody CustomerDTO dto, Pageable pageable) {

        return ResponseUtil.ok(this.customerService.findByRequirement(dto, pageable));
    }

    @GetMapping("findByNameOrPhone")
    private ResponseEntity<?> findByNameOrPhone(@RequestParam String request) {
        return customerService.findByNamePhone(request);
    }

    @GetMapping("/find-id")
    private ResponseEntity<?> findID(@RequestParam Long id) {
        return ResponseEntity.ok(this.customerService.findByID(id));
    }

    @GetMapping(value = "/export", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<?> downloadFile(HttpServletResponse response) throws IOException {
        List<Customers> customersList = this.customerService.findByDeleted();
        ExportCustomer exportCustomer = new ExportCustomer(new ArrayList<>(customersList));
        ByteArrayInputStream byteArrayInputStream = exportCustomer.export();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=DSKH.xlsx");
        IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        return ResponseEntity.ok()
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<?> importFromExcelToDb(@RequestPart("file") MultipartFile files) {
        this.customerService.importExcel(files);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/search-name-phone")
    private ResponseEntity<?> findByRequirement(@RequestParam("search") String search) {

        return ResponseUtil.ok(this.customerService.findByNameOrPhone(search));
    }

    @PostMapping("/change-group")
    private ResponseEntity<?> changeGroup(@RequestBody List<Long> idDTO, @RequestParam("idGroup") Long idGroup) {

        return ResponseUtil.ok(this.customerService.findByCustomerGroup(idDTO, idGroup));
    }


}
