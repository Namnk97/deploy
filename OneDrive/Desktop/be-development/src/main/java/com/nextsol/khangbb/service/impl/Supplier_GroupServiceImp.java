package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.entity.Supplier_Group;
import com.nextsol.khangbb.exception.BadRequestException;
import com.nextsol.khangbb.model.Supplier_GroupDTO;
import com.nextsol.khangbb.repository.Supplier_GroupRepository;
import com.nextsol.khangbb.service.Supplier_GroupService;
import com.nextsol.khangbb.util.MapperUtil;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class Supplier_GroupServiceImp implements Supplier_GroupService {


    private final Supplier_GroupRepository supplier_groupRepository;

    @Override
    public Page<Supplier_GroupDTO> findAll(Pageable pageable) {

        return null;
    }

    @Override
    public Optional<Supplier_GroupDTO> findById(Long id) {
        Optional<Supplier_Group> group = this.supplier_groupRepository.findById(id);
        if (group.isPresent()) {
            return Optional.of(toGroupDTO(group.get()));
        }
        return Optional.empty();
    }

    @Override
    public Supplier_GroupDTO create(Supplier_GroupDTO supplier_groupDTO) {
        Supplier_Group check = this.supplier_groupRepository.findByDeletedAndName(0, supplier_groupDTO.getName()).orElse(null);
        if (check != null) {
            throw new BadRequestException("Name repeat again");
        }
        Supplier_Group supplier_group = MapperUtil.map(supplier_groupDTO, Supplier_Group.class);
        supplier_group.setDeleted(0);
        Supplier_Group supplierGroup = this.supplier_groupRepository.save(supplier_group);
        Supplier_GroupDTO dto = MapperUtil.map(supplierGroup, Supplier_GroupDTO.class);
        return dto;
    }

    @Override
    public List<Supplier_GroupDTO> findByAll() {
        List<Supplier_Group> list = this.supplier_groupRepository.findByDeleted(0);
        List<Supplier_GroupDTO> dtoList = MapperUtil.mapList(list, Supplier_GroupDTO.class);
        return dtoList;
    }

    @Override
    public Supplier_GroupDTO update(Supplier_GroupDTO dto) {
        Supplier_Group check = this.supplier_groupRepository.findByDeletedAndName(0, dto.getName()).orElse(null);
        if (check != null) {
            throw new BadRequestException("Name repeat again");
        }
        Supplier_Group group = this.supplier_groupRepository.findById(dto.getId()).get();
        group.setDescription(dto.getDescription());
        group.setName(dto.getName());
        group.setDeleted(0);
        this.supplier_groupRepository.save(group);
        return dto;
    }

    @Override
    public Supplier_GroupDTO toGroupDTO(Supplier_Group group) {
        if (group != null) {
            return MapperUtil.map(group, Supplier_GroupDTO.class);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> remove(Long id) {
        if (!findById(id).isEmpty()) {
            Supplier_Group group = this.supplier_groupRepository.findById(id).orElseThrow();
            if (group.getDeleted() == 1) {
                return ResponseUtil.notFound(HttpStatus.NOT_FOUND);
            }
            group.setDeleted(1);
            this.supplier_groupRepository.save(group);
        }
        return ResponseUtil.ok(HttpStatus.OK);
    }
}
