package com.nextsol.khangbb.controller;

import com.nextsol.khangbb.entity.ProductExcel;
import com.nextsol.khangbb.entity.Product_GroupExcel;
import com.nextsol.khangbb.entity.Product_Groups;
import com.nextsol.khangbb.entity.Products;
import com.nextsol.khangbb.model.CodeDTO;
import com.nextsol.khangbb.model.ProductDTO;
import com.nextsol.khangbb.repository.ExcelProductRepository;
import com.nextsol.khangbb.repository.ProductGroupExcelRepository;
import com.nextsol.khangbb.repository.ProductsRepository;
import com.nextsol.khangbb.service.ProductService;
import com.nextsol.khangbb.service.Product_GroupService;
import com.nextsol.khangbb.util.ExportInventory;
import com.nextsol.khangbb.util.ExportProduct;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.*;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
@RequestMapping("/api/v1/product")
public class ProductAPI {

    private final ProductService productService;
    private final Product_GroupService product_groupService;
    private final ExcelProductRepository excelProductRepository;
    private final ProductGroupExcelRepository productGroupExcelRepository;

    @GetMapping(value = "/export", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<?> downloadFile(HttpServletResponse response) throws IOException {
        List<ProductExcel> list = this.excelProductRepository.findByDeleted(0);
        List<Product_GroupExcel> listGroup = productGroupExcelRepository.findByDeleted(0);
        ExportProduct exportProduct = new ExportProduct(list,listGroup);
        ByteArrayInputStream byteArrayInputStream = exportProduct.export();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=DSSP.xlsx");
        IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        return ResponseEntity.ok()
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @GetMapping(value = "/export-inventory", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<?> downloadFileInventory(HttpServletResponse response) throws IOException {
        List<Products> list = this.productService.exportInventory();
        ExportInventory exportInventory = new ExportInventory(new ArrayList<>(list));
        ByteArrayInputStream byteArrayInputStream = exportInventory.export();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=DSSP.xlsx");
        IOUtils.copy(byteArrayInputStream, response.getOutputStream());
        return ResponseEntity.ok()
                .body(new InputStreamResource(byteArrayInputStream));
    }

    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<?> importFromExcelToDb(@RequestPart("file") MultipartFile files) {
        this.productService.importToDb(files);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<?> findRequirement(@RequestBody ProductDTO dto, Pageable pageable) {
        return ResponseUtil.ok(this.productService.findRequirement(dto, pageable));
    }

    @PostMapping("/search-warehouse")
    public ResponseEntity<?> findByImportWarehouse(@RequestBody ProductDTO dto) {
        return ResponseUtil.ok(this.productService.findByImportWarehouse(dto));
    }

    @PostMapping("/create-inventory")
    public ResponseEntity<?> saveInventory(@RequestBody List<ProductDTO> dto) {
        return ResponseUtil.ok(this.productService.saveInventory(dto));
    }

    @GetMapping("/find-branch-user")
    public ResponseEntity<?> findByRequestFromUserBranch(@RequestParam String request) {
        return ResponseEntity.ok(this.productService.findByNameCodeFromBranchUser(request));
    }

    @GetMapping("/branch-user")
    public ResponseEntity<?> findByUserBranch() {
        return ResponseEntity.ok(this.productService.findByBranchUser());
    }


    @GetMapping("/branch")
    public ResponseEntity<?> searchByWarehouse(@RequestParam(required = false) Long id, Pageable pageable) {

        return ResponseEntity.ok(this.productService.findByBranch(id, pageable));
    }


    @GetMapping("/choose-branch")
    public ResponseEntity<?> findByBranchToPayment(@RequestParam(required = false) Long idGroup) {
        return ResponseEntity.ok(this.productService
                .findByBranch(idGroup));
    }

    @GetMapping
    private ResponseEntity<?> findAll(Pageable pageable) {
        return ResponseUtil.ok(this.productService.findAll(pageable));
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> findById(@PathVariable Long id) {

        return ResponseUtil.ok(this.productService.findById(id));
    }

    @GetMapping("/filter")
    private ResponseEntity<?> filter(@RequestParam String request, @RequestParam @Nullable Long id) {
        return ResponseUtil.ok(productService.filter(request, id));
    }

    @GetMapping("app/filter")
    private ResponseEntity<?> filterApp(Pageable pageable, @RequestParam String request) {
        return productService.filterApp(pageable, request);
    }

    @PostMapping
    private ResponseEntity<?> save(@Valid @RequestBody ProductDTO dto) {
        return ResponseUtil.ok(this.productService.create(dto));
    }

    @PutMapping
    private ResponseEntity<?> update(@Valid @RequestBody ProductDTO dto) {
        return ResponseUtil.ok(this.productService.update(dto));
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
    private ResponseEntity<?> delete(@RequestBody ProductDTO dto) {

        return this.productService.remove(dto.getId());
    }

    @PostMapping("/move-branch")
    private ResponseEntity<?> moveToWarehouseBranch(@RequestBody ImportBranch importBranch, @RequestParam Long idBranchTarget) {
        return this.productService.moveToBranch(importBranch.getCodeDTO(), importBranch.getProductDTOList(), idBranchTarget);
    }

    @PostMapping("/import-warehouse")
    private ResponseEntity<?> importProductToWarehouse(@RequestBody ImportBranch importBranch) {
        return this.productService.importProductToWarehouse(importBranch.getCodeDTO(), importBranch.getProductDTOList());
    }

    @GetMapping("/find-supplier")
    private ResponseEntity<?> findBySupplier(@RequestParam(required = false) Long idSupplier) {
        return ResponseUtil.ok(this.productService.findBySupplier(idSupplier));
    }

    @PostMapping("/pay-supplier")
    private ResponseEntity<?> paySupplier(@RequestBody ImportBranch importBranch) {
        return this.productService.paySupplier(importBranch.getCodeDTO(), importBranch.getProductDTOList());
    }


}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ImportBranch {
    private CodeDTO codeDTO;
    private List<ProductDTO> productDTOList;
    private String request;
    private Long idGroup;
}


