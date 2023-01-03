package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.entity.Customers;
import com.nextsol.khangbb.entity.Customers_Groups;
import com.nextsol.khangbb.exception.BadRequestException;
import com.nextsol.khangbb.model.CustomerDTO;
import com.nextsol.khangbb.repository.CustomersRepository;
import com.nextsol.khangbb.repository.Customers_GroupRepository;
import com.nextsol.khangbb.service.CustomerService;
import com.nextsol.khangbb.util.DateUtil;
import com.nextsol.khangbb.util.MapperUtil;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {


    private final CustomersRepository customersRepository;


    private final Customers_GroupRepository customers_groupRepository;

    @Override
    public Page<CustomerDTO> findAll(Pageable pageable) {
        Page<Customers> customers = this.customersRepository.findByDeleted(0, pageable);
        Page<CustomerDTO> customerDTO = MapperUtil.mapEntityPageIntoDtoPage(customers, CustomerDTO.class);
        return customerDTO;
    }

    @Override
    public Optional<CustomerDTO> findById(Long id) {
        Optional<Customers> customer = this.customersRepository.findById(id);
        if (customer.isPresent()) {
            return Optional.of(toCustomerDTO(customer.get()));
        }
        return Optional.empty();
    }

    @Override
    public CustomerDTO toCustomerDTO(Customers customer) {
        if (customer != null) {
            return MapperUtil.map(customer, CustomerDTO.class);
        }
        return null;
    }

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        Customers check = this.customersRepository.findByPhoneAndDeleted(customerDTO.getPhone(), 0);
        if (check != null) {
            throw new BadRequestException("Phone repeat again");
        }
        Customers_Groups customerGroup = this.customers_groupRepository.findById(customerDTO.getIdGroup()).orElseThrow();
        Customers customer = MapperUtil.map(customerDTO, Customers.class);
        customer.setDeleted(0);
        customer.setDebit(BigDecimal.valueOf(0));
        customer.setNumberPurchase(0);
        customer.setPoint(0);
        customer.setTurnover(BigDecimal.valueOf(0));
        customer.setCustomerGroup(customerGroup);
        Customers customers1 = this.customersRepository.save(customer);
        return MapperUtil.map(customers1, CustomerDTO.class);
    }

    @Override
    public ResponseEntity<?> remove(Long id) {
        if (!findById(id).isEmpty()) {
            Customers customers = this.customersRepository.findById(id).orElseThrow();
            if (customers.getDeleted() == 1) {
                return ResponseUtil.notFound(HttpStatus.NOT_FOUND);
            }
            customers.setDeleted(1);
            this.customersRepository.save(customers);
        }
        return ResponseUtil.ok(HttpStatus.OK);
    }

    @Override
    public CustomerDTO update(CustomerDTO dto) {
        Customers check = this.customersRepository.findByPhoneAndDeleted(dto.getPhone(), 0);
        if (check != null) {
            if (check.getId() != dto.getId()) {
                throw new BadRequestException("Phone không được trùng");
            }
        }
        Customers customers = this.customersRepository.findById(dto.getId()).orElseThrow();
        Customers_Groups customerGroup = this.customers_groupRepository.findById(dto.getIdGroup()).orElseThrow();
        customers.setCustomerGroup(customerGroup);
        customers = MapperUtil.map(dto, Customers.class);
        this.customersRepository.save(customers);
        return MapperUtil.map(customers, CustomerDTO.class);
    }

    @Override
    public Page<CustomerDTO> findByRequirement(CustomerDTO dto, Pageable pageable) {
        Page<CustomerDTO> customerPage = this.customersRepository.findRequirementCustomer(dto, pageable);
        return customerPage;
    }

    @Override
    public List<Customers> findByDeleted() {
        List<Customers> customersList = this.customersRepository.findByDeleted(0);
        return customersList;
    }

    @Override
    public ResponseEntity<?> findByNamePhone(String request) {
        return ResponseUtil.ok(customersRepository.findByNameOrPhone(request));
    }

    @Override
    public void importExcel(MultipartFile multipartfiles) {
        List<Customers> customersList = new ArrayList<>();
        if (!multipartfiles.isEmpty()) {
            try {
                XSSFWorkbook workBook = new XSSFWorkbook(multipartfiles.getInputStream());
                XSSFSheet sheet = workBook.getSheetAt(0);
                for (int rowIndex = 0; rowIndex < getNumberOfNonEmptyCells(sheet, 0); rowIndex++) {

                    XSSFRow row = sheet.getRow(rowIndex);

                    if (rowIndex == 0) {
                        continue;
                    }
                    String fullname = String.valueOf(row.getCell(0));
                    String phone = String.valueOf(row.getCell(1));
                    String address = String.valueOf(row.getCell(2));
                    String born = String.valueOf(row.getCell(3));
                    String cell4 = String.valueOf(row.getCell(4));
                    Integer sexx;
                    if (cell4 != null && !cell4.isEmpty()) {
                        sexx = new BigDecimal(String.valueOf(cell4)).toBigInteger().intValue();
                    } else {
                        sexx = 3;
                    }
                    String note = String.valueOf(row.getCell(5));
                    String nameGroup = String.valueOf(row.getCell(6));
                    Customers_Groups customers_groups = this.customers_groupRepository.findByName(nameGroup);
                    String email = String.valueOf(row.getCell(7));
                    try {
                        Customers customers = Customers.builder()
                                .fullname(fullname)
                                .phone(phone)
                                .address(address)
                                .birthday(DateUtil.sdf.parse(born))
                                .sex(sexx)
                                .description(note)
                                .customerGroup(customers_groups)
                                .email(email)
                                .debit(BigDecimal.valueOf(0))
                                .turnover(BigDecimal.valueOf(0))
                                .numberPurchase(0)
                                .point(0)
                                .deleted(0)
                                .build();
                        customersList.add(customers);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!customersList.isEmpty()) {
            List<Customers> list = new ArrayList<>();
            for (Customers cus : customersList) {
                Customers customers = this.customersRepository.findByPhone(cus.getPhone());
                if (customers == null) {
                    list.add(cus);
                }
            }
            this.customersRepository.saveAll(list);
        }
    }

    @Override
    public Customers save(CustomerDTO dto) {
        return customersRepository.save(new Customers(dto.getFullname(), dto.getPhone(), dto.getAddress(), dto.getEmail()));
    }

    @Override
    public List<CustomerDTO> findByNameOrPhone(String search) {
        List<Customers> customersList = this.customersRepository.findByFullNameOrPhone(search);
        List<CustomerDTO> dtoList = MapperUtil.mapList(customersList, CustomerDTO.class);
        return dtoList;
    }

    @Override
    public List<CustomerDTO> findByCustomerGroup(List<Long> idDTO, Long idGroup) {
        List<Customers> listEnd = new ArrayList<>();
        List<Customers> lstCustomer = this.customersRepository.findByIdIn(idDTO);
        boolean check = checkGroup(lstCustomer, idGroup);
        if (!check) {
            throw new BadRequestException("Same of Customer Group");
        }

        for (Customers cus : lstCustomer) {
            Customers_Groups groups = this.customers_groupRepository.findById(idGroup).orElseThrow();
            cus.setCustomerGroup(groups);
            listEnd.add(cus);
        }
        this.customersRepository.saveAll(listEnd);
        return MapperUtil.mapList(lstCustomer, CustomerDTO.class);
    }


    private boolean checkGroup(List<Customers> cusList, Long idGroupTarget) {
        for (Customers cus : cusList) {
            if (cus.getCustomerGroup().getId() != idGroupTarget) {
                return true;
            }
        }
        return false;
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

    @Override
    public Customers findByPhone(String phone) {
        return customersRepository.findByPhone(phone);
    }

    @Override
    public CustomerDTO findByID(Long id) {
        Customers customers = this.customersRepository.findById(id).orElseThrow();
        CustomerDTO dto = MapperUtil.map(customers, CustomerDTO.class);
        return dto;
    }
}
