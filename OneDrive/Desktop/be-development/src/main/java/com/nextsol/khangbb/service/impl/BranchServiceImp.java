package com.nextsol.khangbb.service.impl;


import com.nextsol.khangbb.entity.Branch;
import com.nextsol.khangbb.entity.Product_Groups;
import com.nextsol.khangbb.entity.Products;
import com.nextsol.khangbb.exception.BadRequestException;
import com.nextsol.khangbb.model.BranchDTO;
import com.nextsol.khangbb.model.ProductInterBranchTransferModel;
import com.nextsol.khangbb.model.ProductInventoryDTO;
import com.nextsol.khangbb.repository.BranchRepository;
import com.nextsol.khangbb.repository.Product_GroupRepository;
import com.nextsol.khangbb.repository.ProductsRepository;
import com.nextsol.khangbb.security.CustomUserDetails;
import com.nextsol.khangbb.service.BranchService;
import com.nextsol.khangbb.service.UserService;
import com.nextsol.khangbb.util.MapperUtil;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BranchServiceImp implements BranchService {

    private final BranchRepository branchRepository;
    private final Product_GroupRepository product_groupRepository;

    private final ProductsRepository productsRepository;
    private final UserService userService;

    @Override
    public Page<BranchDTO> findAll(Pageable pageable) {
        Page<Branch> branchPage = this.branchRepository.findAll(pageable);
        Page<BranchDTO> branchDTOS = MapperUtil.mapEntityPageIntoDtoPage(branchPage, BranchDTO.class);
        return branchDTOS;
    }

    @Override
    public List<BranchDTO> findByDeleted() {
        List<Branch> branchList = this.branchRepository.findByDeleted(0);
        List<BranchDTO> dtoList = MapperUtil.mapList(branchList, BranchDTO.class);
        return dtoList;
    }

    @Override
    public ResponseEntity<?> remove(Long id) {
        if (!findById(id).isEmpty()) {
            Branch branch = this.branchRepository.findById(id).orElseThrow();
            if (branch.getDeleted() == 1) {
                return ResponseUtil.notFound(HttpStatus.NOT_FOUND);
            }
            branch.setDeleted(1);
            this.branchRepository.save(branch);
        }
        return ResponseUtil.ok(HttpStatus.OK);
    }

    @Override
    public BranchDTO toBranchDTO(Branch branch) {
        if (branch != null) {
            return MapperUtil.map(branch, BranchDTO.class);
        }
        return null;
    }


    @Override
    public ResponseEntity<?> importExcelShopProduct(MultipartFile file) throws IOException {
        try {
            List<Products> list = new ArrayList<>();
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet worksheet = workbook.getSheetAt(0);
            if (worksheet.getLastRowNum() > 0) {
                List<Product_Groups> product_groups = product_groupRepository.findAll();
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    Products products = new Products();
                    Row row = worksheet.getRow(i);
                    if (row.getCell(0) == null | row.getCell(1) == null | row.getCell(3) == null | row.getCell(4) == null | row.getCell(5) == null)
                        break;
                    for (Product_Groups item : product_groups) {
                        if (item.getCode().equals(row.getCell(3).getStringCellValue().trim()))
                            products.setProductGroup(item);
                    }
                    String code = row.getCell(0).getStringCellValue().trim();
                    String name = row.getCell(0).getStringCellValue().trim();
                    BigDecimal importPrice = BigDecimal.valueOf(row.getCell(4).getNumericCellValue());
                    Optional<Products> optional = productsRepository.findByCodeAndNameAndImportPriceAndBranch(code, name, importPrice, products.getBranch());
                    if (optional.isPresent())
                        products = optional.get();
                    products.setCode(code);
                    products.setName(name);
                    products.setUnit(row.getCell(2).getStringCellValue().trim());
                    products.setImportPrice(importPrice);
                    products.setRetail_price(BigDecimal.valueOf(row.getCell(5).getNumericCellValue()));
                    products.setWhole_price(BigDecimal.valueOf(row.getCell(6).getNumericCellValue()));
                    products.setDiscount(row.getCell(7).getNumericCellValue());
                    products.setDiscountMoney(BigDecimal.valueOf(row.getCell(8).getNumericCellValue()));
                    products.setQuantity((int) row.getCell(9).getNumericCellValue());
                    products.setDescription(row.getCell(10).getStringCellValue().trim());
                    list.add(products);
                }
                return ResponseUtil.ok(list);
            }
            return ResponseUtil.noContent(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.noContent(null);
        }
    }

    @Override
    public ResponseEntity<?> importExcelInventory(MultipartFile file) throws IOException {
        try {
            List<ProductInventoryDTO> list = new ArrayList<>();
            CustomUserDetails users = (CustomUserDetails) this.userService.getInfoUser();
            List<Products> products = productsRepository.findByBranchAndDeleted(users.getUsers().getBranch(), 0);
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet worksheet = workbook.getSheetAt(0);
            if (worksheet.getLastRowNum() > 0) {
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    ProductInventoryDTO productDTO;
                    Row row = worksheet.getRow(i);
                    if (row.getCell(0) == null && row.getCell(1) == null)
                        continue;
                    String code = row.getCell(0).getStringCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    Products product = products.stream().filter(x -> x.getCode().equals(code) && x.getName().equals(name)).findAny().orElse(null);
                    if (product == null)
                        continue;
                    productDTO = MapperUtil.map(product, ProductInventoryDTO.class);
                    productDTO.setUnit(row.getCell(2).getStringCellValue());
                    productDTO.setImportPrice(new BigDecimal(row.getCell(3).getStringCellValue()));
                    productDTO.setQuantityFrom((int)row.getCell(5).getNumericCellValue());
                    productDTO.setDescription(row.getCell(6).getStringCellValue());
                    list.add(productDTO);
                }
                return ResponseUtil.ok(list);
            }
            return ResponseUtil.badRequest("File Excel không có bản ghi");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseUtil.badRequest("File Excel không hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.badRequest(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> importInterBranchTransfer(MultipartFile file) throws IOException {
        try {
            List<ProductInterBranchTransferModel> list = new ArrayList<>();
            CustomUserDetails users = (CustomUserDetails) this.userService.getInfoUser();
            List<Products> products = productsRepository.findByBranch(users.getUsers().getBranch());
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet worksheet = workbook.getSheetAt(0);
            if (worksheet.getLastRowNum() > 0) {
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    Row row = worksheet.getRow(i);
                    String code = row.getCell(0).getStringCellValue().trim();
                    String name = row.getCell(1).getStringCellValue().trim();
                    BigDecimal price = BigDecimal.valueOf(row.getCell(2).getNumericCellValue());
                    Products product = products.stream().filter(x -> x.getCode().equals(code)
                            && x.getName().equals(name)
                            && x.getImportPrice().equals(price)).findAny().orElse(null);
                    if (product == null)
                        continue;
                    ProductInterBranchTransferModel model = MapperUtil.map(products, ProductInterBranchTransferModel.class);
                    model.setCode(code);
                    model.setName(name);
                    model.setImport_price(price);
                    model.setQuantityTransfer((int) row.getCell(3).getNumericCellValue());
                    model.setDescription(row.getCell(4).getStringCellValue());
                    list.add(model);
                }
                return ResponseUtil.ok(list);
            }
            return ResponseUtil.noContent(null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseUtil.badRequest("File Excel không hợp lệ");
        }
    }

    @Override
    public void saveList(List<Products> products) {
        productsRepository.saveAllAndFlush(products);
    }

    @Override
    public Optional<BranchDTO> findById(Long id) {
        Optional<Branch> branch = this.branchRepository.findById(id);
        if (branch.isPresent()) {
            return Optional.of(toBranchDTO(branch.get()));
        }
        return Optional.empty();
    }

    @Override
    public BranchDTO create(BranchDTO branchDTO) {

        Branch check = this.branchRepository.findByNameAndDeleted(branchDTO.getName(), 0);
        if (check != null) {
            throw new BadRequestException("Name repeat again");
        }
        Branch branch = MapperUtil.map(branchDTO, Branch.class);
        branch.setActived(branchDTO.getActived());
        branch.setName(branchDTO.getName());
        branch.setAddress(branchDTO.getAddress());
        branch.setCity(branchDTO.getCity());
        branch.setDescription(branchDTO.getDescription());
        branch.setEmail(branchDTO.getEmail());
        branch.setPhone(branchDTO.getPhone());
        branch.setDeleted(0);
        this.branchRepository.save(branch);
        BranchDTO dto = MapperUtil.map(branch, BranchDTO.class);
        return dto;
    }

    @Override
    public BranchDTO update(BranchDTO dto) {

        Branch branch = this.branchRepository.findByIdAndDeleted(dto.getId(), 0);
        Branch check = this.branchRepository.findByNameAndDeleted(dto.getName(), 0);
        if(check.getId()!= dto.getId() && check !=null){
            throw new BadRequestException("Không được trùng tên chi nhánh");
        }
        branch.setDeleted(0);
        branch.setPhone(dto.getPhone());
        branch.setEmail(dto.getEmail());
        branch.setCity(dto.getCity());
        branch.setDescription(dto.getDescription());
        branch.setActived(dto.getActived());
        branch.setName(dto.getName());
        branch.setAddress(dto.getAddress());
        Branch branch1 = this.branchRepository.save(branch);
        return MapperUtil.map(branch1, BranchDTO.class);
    }


}
