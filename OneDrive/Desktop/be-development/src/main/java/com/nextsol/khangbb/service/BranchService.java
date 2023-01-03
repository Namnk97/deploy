package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Branch;
import com.nextsol.khangbb.entity.Products;
import com.nextsol.khangbb.model.BranchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BranchService extends BaseService<BranchDTO> {

    Page<BranchDTO> findAll(Pageable pageable);

    List<BranchDTO> findByDeleted();

    ResponseEntity<?> remove(Long id);

    BranchDTO toBranchDTO(Branch branch);

    BranchDTO update(BranchDTO dto);

    ResponseEntity<?> importExcelShopProduct(MultipartFile file) throws IOException;

    ResponseEntity<?> importExcelInventory(MultipartFile file) throws IOException;


    ResponseEntity<?> importInterBranchTransfer(MultipartFile file) throws IOException;

    void saveList(List<Products> products);
}
