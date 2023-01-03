package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Supplier_Group;
import com.nextsol.khangbb.model.Supplier_GroupDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface Supplier_GroupService extends BaseService<Supplier_GroupDTO> {

    List<Supplier_GroupDTO> findByAll();

    Supplier_GroupDTO update(Supplier_GroupDTO dto);

    Supplier_GroupDTO toGroupDTO(Supplier_Group group);

    ResponseEntity<?> remove(Long id);
}
