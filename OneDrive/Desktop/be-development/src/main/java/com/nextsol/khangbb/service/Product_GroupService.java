package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Product_Groups;
import com.nextsol.khangbb.model.Product_GroupDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface Product_GroupService extends BaseService<Product_GroupDTO> {

    ResponseEntity<?> save(Product_GroupDTO dto);
    ResponseEntity<?> update(Product_GroupDTO dto);

    List<Product_Groups> find(Integer deleted);

    void importToDb(MultipartFile multipartfiles);

    ResponseEntity<?> delete(Long id);

    Product_GroupDTO toProduct_GroupDTO(Product_Groups product_groups);

    Optional<Product_Groups> findByCode(String code);

}
