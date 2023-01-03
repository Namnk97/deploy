package com.nextsol.khangbb.controller;


import com.nextsol.khangbb.entity.Product_Groups;
import com.nextsol.khangbb.model.Product_GroupDTO;
import com.nextsol.khangbb.service.Product_GroupService;
import com.nextsol.khangbb.util.ExportProduct_Group;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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
@RequestMapping("/api/v1/product-group")
public class Product_GroupAPI {

    private final Product_GroupService product_groupService;


    @GetMapping
    private ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseUtil.ok(product_groupService.findAll(pageable));
    }

    @PostMapping
    private ResponseEntity<?> createProduct_Group(@Valid @RequestBody Product_GroupDTO dto) {
        return product_groupService.save(dto);
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
    private ResponseEntity<?> updateProduct_Group(@Valid @RequestBody Product_GroupDTO dto) {
        return product_groupService.update(dto);
    }

    @DeleteMapping
    private ResponseEntity<?> deleteProduct_Group(@RequestBody Product_GroupDTO dto) {
        return this.product_groupService.delete(dto.getId());
    }

    @GetMapping(value = "/export", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<?> downloadFile(HttpServletResponse response) throws IOException {
        List<Product_Groups> list = this.product_groupService.find(0);
        ExportProduct_Group exportProduct_group = new ExportProduct_Group(new ArrayList<>(list));
        ByteArrayInputStream byteArrayInputStream = exportProduct_group.export();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=DanhSachNhomSanPham.xlsx");
        IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        return ResponseEntity.ok()
                .body(new InputStreamResource(byteArrayInputStream));
    }


    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<?> importFromExcelToDb(@RequestPart("file") MultipartFile files) {
        this.product_groupService.importToDb(files);
        return ResponseEntity.ok(HttpStatus.OK);
    }


}


