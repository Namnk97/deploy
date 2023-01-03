package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.entity.Customers_Groups;
import com.nextsol.khangbb.exception.BadRequestException;
import com.nextsol.khangbb.model.Customer_GroupDTO;
import com.nextsol.khangbb.repository.CustomersRepository;
import com.nextsol.khangbb.repository.Customers_GroupRepository;
import com.nextsol.khangbb.service.Customer_GroupService;
import com.nextsol.khangbb.util.MapperUtil;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class Customer_GroupServiceImp implements Customer_GroupService {


    private final Customers_GroupRepository customers_groupRepository;


    private final CustomersRepository customersRepository;

    @Override
    public List<Customer_GroupDTO> findByAll() {
        List<Customers_Groups> customers_groupsList = this.customers_groupRepository.findByDeleted(0);
        Map<Long, Integer> sumCount = new HashMap<>();
        customers_groupsList.forEach(customersGroups -> {
            Integer group = this.customersRepository.countByCustomerGroup(customersGroups);
            sumCount.put(customersGroups.getId(), group);

        });
        List<Customer_GroupDTO> dtoList = MapperUtil.mapList(customers_groupsList, Customer_GroupDTO.class);
        dtoList.forEach(dto -> {
            //Map get key return value
            dto.setQuantity(sumCount.get(dto.getId()));

        });

        return dtoList;
    }

    @Override
    public Customer_GroupDTO create(Customer_GroupDTO dto) {
        Customers_Groups check = this.customers_groupRepository.findByNameAndDeleted(dto.getName(), 0);
        if (check != null) {
            throw new BadRequestException("Name repeat again");
        }
        Customers_Groups customers_groups = MapperUtil.map(dto, Customers_Groups.class);
        customers_groups.setDeleted(0);
        Customers_Groups customers_groups1 = this.customers_groupRepository.save(customers_groups);
        Customer_GroupDTO customerGroupDTO = MapperUtil.map(customers_groups1, Customer_GroupDTO.class);
        return customerGroupDTO;
    }

    @Override
    public ResponseEntity<?> update(Customer_GroupDTO dto) {
        Customers_Groups check = this.customers_groupRepository.findByNameAndDeleted(dto.getName(), 0);
        if (check.getId() != dto.getId() && check != null) {
            throw new BadRequestException("Name repeat again");
        }
        if (this.customers_groupRepository.findById(dto.getId()).get() == null) {
            throw new BadRequestException("Not find ID");
        }
        Customers_Groups customers_groups = MapperUtil.map(dto, Customers_Groups.class);
        customers_groups.setDeleted(0);
        this.customers_groupRepository.save(customers_groups);
        return ResponseUtil.ok(HttpStatus.OK);
    }

    @Override
    public Customer_GroupDTO toCustomerGroupDTO(Customers_Groups customersGroups) {
        if (customersGroups != null) {
            return MapperUtil.map(customersGroups, Customer_GroupDTO.class);
        }
        return null;
    }

    @Override
    public Optional<Customer_GroupDTO> findById(Long id) {
        Optional<Customers_Groups> customers_groups = this.customers_groupRepository.findById(id);
        if (customers_groups.isPresent()) {
            return Optional.of(toCustomerGroupDTO(customers_groups.get()));
        }
        return Optional.empty();
    }

    @Override
    public ResponseEntity<?> remove(Long id) {
        if (!findById(id).isEmpty()) {
            Customers_Groups customers_groups = this.customers_groupRepository.findById(id).orElseThrow();
            if (customers_groups.getDeleted() == 1) {
                return ResponseUtil.notFound(HttpStatus.NOT_FOUND);
            }
            customers_groups.setDeleted(1);
            this.customers_groupRepository.save(customers_groups);
        }
        return ResponseUtil.ok(HttpStatus.OK);
    }


}
