package com.nextsol.khangbb.service.impl;


import com.nextsol.khangbb.entity.Supplier;
import com.nextsol.khangbb.entity.Supplier_Group;
import com.nextsol.khangbb.exception.BadRequestException;
import com.nextsol.khangbb.model.SupplierDTO;
import com.nextsol.khangbb.repository.SupplierRepository;
import com.nextsol.khangbb.repository.Supplier_GroupRepository;
import com.nextsol.khangbb.service.SupplierService;
import com.nextsol.khangbb.util.MapperUtil;
import com.nextsol.khangbb.util.ResponseUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SupplierServiceImp implements SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private Supplier_GroupRepository groupRepository;


    @Override
    public List<SupplierDTO> findByAll() {
        List<Supplier> supplier = this.supplierRepository.findAll();
        List<SupplierDTO> supplierDTO = MapperUtil.mapList(supplier, SupplierDTO.class);

        return supplierDTO;
    }

    @Override
    public List<Supplier> findALL() {
        return this.supplierRepository.findByDeleted(0);
    }

    @Override
    public SupplierDTO update(SupplierDTO dto) {
        Supplier check = this.supplierRepository.findByNameAndDeleted(dto.getName(), 0);
        if(check != null) {
            if (!Objects.equals(check.getId(), dto.getId()) ) {
                throw new BadRequestException("Không được trùng tên nhà cung cấp");
            }
        }
        Supplier supplier = this.supplierRepository.findById(dto.getId()).orElseThrow();
        Supplier_Group group = this.groupRepository.findById(dto.getIdGroup()).orElseThrow();
        supplier.setDeleted(0);
        supplier.setGroup(group);
        supplier.setName(dto.getName());
        supplier.setAddress(dto.getAddress());
        supplier.setFax(dto.getFax());
        supplier.setDescription(dto.getDescription());
        supplier.setEmail(dto.getEmail());
        supplier.setPhone(dto.getPhone());
        supplier.setContactName(dto.getContactName());
        this.supplierRepository.save(supplier);
        return dto;
    }

    @Override
    public SupplierDTO toSupplierDTO(Supplier supplier) {
        if (supplier != null) {
            return MapperUtil.map(supplier, SupplierDTO.class);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> remove(Long id) {
        if (!findById(id).isEmpty()) {
            Supplier supplier = this.supplierRepository.findById(id).orElseThrow();
            if (supplier.getDeleted() == 1) {
                return ResponseUtil.notFound(HttpStatus.NOT_FOUND);
            }
            supplier.setDeleted(1);
            this.supplierRepository.save(supplier);
        }
        return ResponseUtil.ok(HttpStatus.OK);
    }

    @Override
    public Page<SupplierDTO> findByRequirement(SupplierDTO dto, Pageable pageable) {
        Page<SupplierDTO> dtoPage = this.supplierRepository.findByRequirement(dto, pageable);
        return dtoPage;
    }


    @Override
    public Page<SupplierDTO> findAll(Pageable pageable) {
        Page<Supplier> supplier = this.supplierRepository.findByDeleted(0, pageable);
        Page<SupplierDTO> supplierDTO = MapperUtil.mapEntityPageIntoDtoPage(supplier, SupplierDTO.class);
        return supplierDTO;
    }


    @Override
    public Optional<SupplierDTO> findById(Long id) {
        Optional<Supplier> supplier = this.supplierRepository.findById(id);
        if (supplier.isPresent()) {
            return Optional.of(toSupplierDTO(supplier.get()));
        }
        return Optional.empty();
    }

    @Override
    public SupplierDTO create(SupplierDTO supplierDTO) {
        Supplier check = this.supplierRepository.findByNameAndDeleted(supplierDTO.getName(), 0);
        if (check != null) {
            throw new BadRequestException("Name repeat again");
        }
        Supplier_Group group = this.groupRepository.findById(supplierDTO.getIdGroup()).orElseThrow();
        Supplier supplier = MapperUtil.map(supplierDTO, Supplier.class);
        supplier.setDeleted(0);
        supplier.setGroup(group);
        this.supplierRepository.save(supplier);
        SupplierDTO dto = MapperUtil.map(supplier, SupplierDTO.class);
        return dto;
    }

    @Override
    public void importExcel(MultipartFile multipartfiles) {
        List<Supplier> supplierList = new ArrayList<>();
        if (!multipartfiles.isEmpty()) {
            try {
                XSSFWorkbook workBook = new XSSFWorkbook(multipartfiles.getInputStream());
                XSSFSheet sheet = workBook.getSheetAt(0);
                for (int rowIndex = 0; rowIndex < getNumberOfNonEmptyCells(sheet, 0); rowIndex++) {

                    XSSFRow row = sheet.getRow(rowIndex);

                    if (rowIndex == 0) {
                        continue;
                    }
                    String name = String.valueOf(row.getCell(0));
                    String address = String.valueOf(row.getCell(1));
                    String contactName = String.valueOf(row.getCell(2));
                    String phone = String.valueOf(row.getCell(3));
                    String fax = String.valueOf(row.getCell(4));
                    String email = String.valueOf(row.getCell(5));
                    String nameGroup = String.valueOf(row.getCell(6));
                    Supplier_Group group = this.groupRepository.findByName(nameGroup);
                    String description = String.valueOf(row.getCell(7));
                    Supplier supplier = Supplier.builder()
                            .name(name)
                            .phone(phone)
                            .address(address)
                            .contactName(contactName)
                            .fax(fax)
                            .description(description)
                            .group(group)
                            .email(email)
                            .deleted(0)
                            .build();
                    supplierList.add(supplier);


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!supplierList.isEmpty()) {
            List<Supplier> list = new ArrayList<>();
            for (Supplier supplier : supplierList) {
                Supplier supp = this.supplierRepository.findByPhone(supplier.getPhone());
                if (supp == null) {
                    list.add(supplier);
                }
            }
            this.supplierRepository.saveAll(list);
        }
    }

    public static int getNumberOfNonEmptyCells(XSSFSheet sheet, int columnIndex) {
        int numOfNonEmptyCells = 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                XSSFCell cell = row.getCell(columnIndex);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    numOfNonEmptyCells++;
                }
            }
        }
        return numOfNonEmptyCells;
    }
}
