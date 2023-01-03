package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Products;
import com.nextsol.khangbb.model.CodeDTO;
import com.nextsol.khangbb.model.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ProductService extends BaseService<ProductDTO> {
    List<Products> find();

    void importToDb(MultipartFile multipartfiles);

    ResponseEntity<?> remove(Long id);

    List<Products> filter(String request, Long id);

    List<ProductDTO> findByNameCodeFromBranchUser(String request);

    ResponseEntity<?> filterApp(Pageable pageable, String request);

    ProductDTO toProductDTO(Products product);

    Page<Products> findByBranch(Long id, Pageable pageable);

    List<ProductDTO> findByBranchUser();

    ResponseEntity<?> moveToBranch(CodeDTO codeDTO, List<ProductDTO> productDTOList, Long idTarget);



    ResponseEntity<?> update(ProductDTO productDTO);

    ResponseEntity<?> importProductToWarehouse(CodeDTO codeDTO, List<ProductDTO> productDTOList);

    Page<ProductDTO> findRequirement(ProductDTO productDTO, Pageable pageable);

    List<ProductDTO> findByImportWarehouse(ProductDTO productDTO);

    List<ProductDTO> saveInventory(List<ProductDTO> dtoList);

    List<Products> exportInventory();


    List<ProductDTO> findByBranch(Long idGroup);

    List<ProductDTO> findBySupplier(Long idSupplier);

    ResponseEntity<?> paySupplier(CodeDTO codeDTO, List<ProductDTO> productDTOList);
}
