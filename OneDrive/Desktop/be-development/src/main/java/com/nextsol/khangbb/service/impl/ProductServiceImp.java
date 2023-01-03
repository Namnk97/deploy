package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.entity.*;
import com.nextsol.khangbb.exception.BadRequestException;
import com.nextsol.khangbb.model.CodeDTO;
import com.nextsol.khangbb.model.CodeDetailDTO;
import com.nextsol.khangbb.model.ProductDTO;
import com.nextsol.khangbb.repository.*;
import com.nextsol.khangbb.security.CustomUserDetails;
import com.nextsol.khangbb.service.CodeService;
import com.nextsol.khangbb.service.EmailService;
import com.nextsol.khangbb.service.ProductService;
import com.nextsol.khangbb.service.UserService;
import com.nextsol.khangbb.util.DataUtil;
import com.nextsol.khangbb.util.DateUtil;
import com.nextsol.khangbb.util.MapperUtil;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductsRepository productsRepository;

    private final Product_GroupRepository product_groupRepository;

    private final BranchRepository branchRepository;


    private final SupplierRepository supplierRepository;

    private final CodeDetailRepository codeDetailRepository;

    private final CodeRepository codeRepository;

    private final UserService userService;

    private final CodeService codeService;
    private final Supplier_DebitRepository supplier_debitRepository;

    private final SupplierDebitHistoryRepository supplierDebitHistoryRepository;

    private final EmailService emailService;

    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Products> productsPage = this.productsRepository.findByDeleted(0, pageable);
        return MapperUtil.mapEntityPageIntoDtoPage(productsPage, ProductDTO.class);
    }

    @Override
    public Optional<ProductDTO> findById(Long id) {
        Optional<Products> products = this.productsRepository.findById(id);
        return products.map(this::toProductDTO);
    }

    @Override
    public ResponseEntity<?> remove(Long id) {
        if (findById(id).isPresent()) {
            Products products = this.productsRepository.findById(id).orElseThrow();
            if (products.getDeleted() == 1) {
                return ResponseUtil.notFound(HttpStatus.NOT_FOUND);
            }
            products.setDeleted(1);
            this.productsRepository.save(products);
        }
        return ResponseUtil.ok(HttpStatus.OK);
    }

    @Override
    public List<Products> filter(String request, Long id) {
        if (id != null) {
            Optional<Branch> optional = branchRepository.findById(id);
            if (optional.isPresent()) {
                Branch branch = optional.get();
                return productsRepository.findByNameOrCode(request, branch);
            }
        }
        return productsRepository.findByNameOrCode(request, null);
    }

    @Override
    public List<ProductDTO> findByNameCodeFromBranchUser(String request) {
        CustomUserDetails users = (CustomUserDetails) this.userService.getInfoUser();
        List<Products> productsList = this.productsRepository.findByNameOrCodeFromBranchUser(request, users.getUsers().getBranch());
        return MapperUtil.mapList(productsList, ProductDTO.class);
    }

    @Override
    public ResponseEntity<?> filterApp(Pageable pageable, String request) {
        return ResponseUtil.ok(productsRepository.findByNameOrCodeApp(pageable, request));
    }

    @Override
    public ProductDTO toProductDTO(Products product) {
        if (product != null) {
            return MapperUtil.map(product, ProductDTO.class);
        }
        return null;
    }

    @Override
    public ProductDTO create(ProductDTO productDTO) {
        Products check = this.productsRepository.findByDeletedAndCode(0, productDTO.getCode()).orElse(null);
        if (check != null) {
            throw new BadRequestException("Repeat Code");
        }
        Product_Groups product_groups = this.product_groupRepository.findById(productDTO.getIdGroup()).orElseThrow();
        Products products = MapperUtil.map(productDTO, Products.class);

        products.setDeleted(0);
        products.setQuantity(1);
        products.setProductGroup(product_groups);
        this.productsRepository.save(products);
        productDTO = MapperUtil.map(products, ProductDTO.class);
        return productDTO;
    }

    @Override
    public List<Products> find() {
        return this.productsRepository.findByDeletedAndBranch();

    }

    @Override
    public void importToDb(MultipartFile multipartfiles) {
        List<Products> productsList = new ArrayList<>();
        if (!multipartfiles.isEmpty()) {
            try {
                XSSFWorkbook workBook = new XSSFWorkbook(multipartfiles.getInputStream());
                XSSFSheet sheet = workBook.getSheetAt(0);
                List<Product_Groups> list = product_groupRepository.findAll();
                Branch branch = null;
                if (branch == null) {

                }

                for (int rowIndex = 0; rowIndex < getNumberOfNonEmptyCells(sheet, 0); rowIndex++) {
                    XSSFRow row = sheet.getRow(rowIndex);
                    if (rowIndex == 0) {
                        continue;
                    }
                    String code = String.valueOf(row.getCell(0));
                    String name = String.valueOf(row.getCell(1));
                    String unit = String.valueOf(row.getCell(2));
                    String codeGroup = String.valueOf(row.getCell(3));
                    Product_Groups product_groups = list.stream().filter(x -> x.getCode().equals(codeGroup)).findAny().orElseThrow();
                    BigDecimal importPrice = new BigDecimal(String.valueOf(row.getCell(4)));
                    BigDecimal retailPrice = new BigDecimal(String.valueOf(row.getCell(5)));
                    BigDecimal wholePrice = new BigDecimal(String.valueOf(row.getCell(6)));
                    Double retail_discount = Double.valueOf(String.valueOf(row.getCell(7)));
                    Double whole_discount = Double.valueOf(String.valueOf(row.getCell(8)));
                    BigDecimal wholeDiscountPrice = new BigDecimal(String.valueOf(row.getCell(9)));
                    BigDecimal retailDiscountPrice = new BigDecimal(String.valueOf(row.getCell(10)));
                    Integer quantity = new BigDecimal(String.valueOf(row.getCell(11))).toBigInteger().intValue();
                    String note = String.valueOf(row.getCell(12));
                    Products product = Products.builder()
                            .name(name)
                            .code(code)
                            .unit(unit)
                            .productGroup(product_groups)
                            .importPrice(importPrice)
                            .retail_price(retailPrice)
                            .whole_price(wholePrice)
                            .retail_discount_money(retailDiscountPrice)
                            .wholesale_discount_money(wholeDiscountPrice)
                            .quantity(quantity)
                            .retail_discount(retail_discount)
                            .wholesale_discount(whole_discount)
                            .note(note)
                            .deleted(0)
                            .actived(0)
                            .build();
                    productsList.add(product);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!productsList.isEmpty()) {
            this.productsRepository.saveAll(productsList);
        }
    }


    @Override
    public Page<Products> findByBranch(Long id, Pageable pageable) {
        Branch branch = this.branchRepository.findById(id).orElseThrow();
        Page<Products> productsPage = this.productsRepository.findByBranch(branch, pageable);
        return productsPage;
    }

    @Override
    public List<ProductDTO> findByBranchUser() {
        CustomUserDetails users = (CustomUserDetails) this.userService.getInfoUser();
        List<Products> list = this.productsRepository.findByBranchAndDeleted(users.getUsers().getBranch(), 0);
        return MapperUtil.mapList(list, ProductDTO.class);
    }


    @Override
    public ResponseEntity<?> update(ProductDTO productDTO) {

        Product_Groups product_groups = this.product_groupRepository.findById(productDTO.getIdGroup()).orElseThrow();
        productsRepository.findById(productDTO.getId()).map(products -> {
            products.setQuantity(productDTO.getQuantity());
            products.setName(productDTO.getName());
            products.setProductGroup(product_groups);
            products.setActived(productDTO.getActived());
            products.setNote(productDTO.getNote());
            products.setImportPrice(productDTO.getImportPrice());
            products.setRetail_price(productDTO.getRetail_price());
            products.setWhole_price(productDTO.getWhole_price());
            products.setRetail_discount(productDTO.getRetail_discount());
            products.setWholesale_discount(productDTO.getWholesale_discount());
            products.setRetail_discount_money(productDTO.getRetail_discount_money());
            products.setWholesale_discount_money(productDTO.getWholesale_discount_money());
            return productsRepository.save(products);
        }).orElseThrow();
        return ResponseUtil.ok(productDTO);
    }

    @Override
    public ResponseEntity<?> importProductToWarehouse(CodeDTO codeDTO, List<ProductDTO> productDTOList) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userService.getInfoUser();
        Users currentUser = customUserDetails.getUsers();
        if (!currentUser.getRole().getCode().equals("ADMIN") && !currentUser.getRole().getCode().equals("VIP")) { // Admin được quyền chuyển hàng trong tất cả chi nhánh
            Branch branch = this.branchRepository.findById(codeDTO.getBranchTo()).orElseThrow();
            Branch currentBranch = currentUser.getBranch();
            if (!Objects.equals(currentBranch.getId(), branch.getId())) {
                throw new BadRequestException("Wrong ID");
            }
        }

        List<Products> productSaveList = new ArrayList<>();
        productDTOList.forEach(productDTO -> {
            if (productDTO.getId() == null) {
                Products products = MapperUtil.map(productDTO, Products.class);
                products.setDeleted(0);
                products.setActived(0);
                productSaveList.add(products);
            }
        });
        List<Products> listProductHasSave = new ArrayList<>();
        if (!productSaveList.isEmpty()) {
            listProductHasSave = this.productsRepository.saveAll(productSaveList);
        }

        List<Products> finalListProductHasSave = listProductHasSave;
        productDTOList.forEach(productDTO -> {
            if (productDTO.getId() != null) {
                finalListProductHasSave.add(MapperUtil.map(productDTO, Products.class));
            }
        });

        Branch branchTarget = this.branchRepository.findById(currentUser.getBranch().getId()).orElseThrow();
        Code code = MapperUtil.map(codeDTO, Code.class);
        code.setBranchTo(currentUser.getBranch().getId());
        code.setBranchFrom(null);
        code.setNote(codeDTO.getNote());
        code.setCode(codeDTO.getCode());
        if (codeDTO.getIdSupplier() != null) {
            Supplier supplier = this.supplierRepository.findById(codeDTO.getIdSupplier()).orElseThrow();
            code.setSupplier(supplier);
            Long totalMoneyPayment = finalListProductHasSave.stream().mapToLong(value -> value.getImportPrice().longValue()).sum();
            if (codeDTO.getMoneyPayment() < totalMoneyPayment) {
                long debit = totalMoneyPayment - codeDTO.getMoneyPayment();
                Supplier_Debit supplierDebit = new Supplier_Debit();
                supplierDebit.setSupplier(supplier);
                supplierDebit.setDebit(BigDecimal.valueOf(totalMoneyPayment));
                supplierDebit.setPay_debit(BigDecimal.valueOf(codeDTO.getMoneyPayment()));
                supplierDebit.setDeleted(0);
                String codeSupplier = "PNM." + supplier.getId() + ".";
                supplierDebit.setCode(codeSupplier);
                supplierDebit = supplier_debitRepository.save(supplierDebit);
                codeSupplier += supplierDebit.getId();
                supplierDebit.setCode(codeSupplier);
                supplier_debitRepository.save(supplierDebit);
                SupplierDebitHistory supplierDebitHistory = new SupplierDebitHistory();
                supplierDebitHistory.setCode(codeSupplier);
                supplierDebitHistory.setSupplierDebit(supplierDebit);
                supplierDebitHistory.setDebit(debit);
                supplierDebitHistory.setMoneyPayment(codeDTO.getMoneyPayment());
                supplierDebitHistory.setMethod("Nợ nhập mua hàng");
                supplierDebitHistoryRepository.save(supplierDebitHistory);
            }
        }

        Code code1 = this.codeRepository.save(code);
        List<Code_Detail> codeDetailList = new LinkedList<>();
        finalListProductHasSave.forEach(productDTO -> {
            Products products = this.productsRepository.findById(productDTO.getId()).orElseThrow();
            Products products_target = this.productsRepository
                    .findByNameAndCodeAndBranch(products.getName(), products.getCode(), branchTarget)
                    .orElse(products);
            products_target.setQuantity(productDTO.getQuantity());
            products_target.setBranch(branchTarget);
            products_target.setImportPrice(productDTO.getImportPrice());
            products_target.setRetail_price(productDTO.getRetail_price());
            products_target.setWhole_price(productDTO.getWhole_price());
            products_target.setDiscount(productDTO.getDiscount());
            products_target.setDiscountMoney(productDTO.getDiscountMoney());
            products_target.setNote(productDTO.getNote());
            Products productNew = this.productsRepository.save(products_target);
            Code_Detail code_detail = new Code_Detail();
            code_detail.setCode(code1);
            code_detail.setProduct(productNew);
            code_detail.setQuantity(productDTO.getQuantity());
            codeDetailList.add(code_detail);
        });
        this.codeDetailRepository.saveAll(codeDetailList);
        return ResponseUtil.ok("OK");
    }

    @Override
    public Page<ProductDTO> findRequirement(ProductDTO productDTO, Pageable pageable) {
        return this.productsRepository.findRequirement(productDTO, pageable);
    }

    @Override
    public List<ProductDTO> findByImportWarehouse(ProductDTO productDTO) {
        return this.productsRepository.findByImportWarehouse(productDTO);
    }

    public ResponseEntity<?> moveToBranch(CodeDTO codeDTO, List<ProductDTO> productDTOList, Long idTarget) {
        List<ProductDTO> listMain = findByBranchUser();
        Long checkBranch = listMain.get(0).getIdBranch();
        List<Products> list = MapperUtil.mapList(listMain, Products.class);
        for (ProductDTO productDTO : productDTOList) {
            Optional<Products> products = list.stream()
                    .filter(productFilter -> Objects.equals(productFilter.getId(), productDTO.getId()))
                    .findAny();
            if (products.isEmpty()) {
                throw new BadRequestException("Sản phẩm không nằm trong chi nhánh");
            }
        }

        Branch branchTarget = this.branchRepository.findById(idTarget).orElseThrow();
        Code code1 = this.codeRepository.save(Code
                .builder()
                .code(this.codeService.rentcode("LCN"))
                .branchTo(idTarget)
                .branchFrom(checkBranch)
                .note(codeDTO.getNote())
                .status(3)
                .type("Nhập điều chuyển")
                .build());
        List<CodeDetailDTO> codeDetailDTOList = new ArrayList<>();
        productDTOList.forEach(productDTO -> {
            Products product = this.productsRepository.findById(productDTO.getId()).orElseThrow();
            Long idBranchMain = product.getBranch().getId();
            if (Objects.equals(idBranchMain, idTarget)) {
                throw new BadRequestException("Không được điều chuyển cùng chi nhánh");
            }

            Code_Detail code_detail = this.codeDetailRepository.save(Code_Detail
                    .builder()
                    .product(product)
                    .quantityReal(productDTO.getQuantity())
                    .quantity(productDTO.getQuantity())
                    .code(code1)
                    .build());
            CodeDetailDTO codeDetailDTO = MapperUtil.map(code_detail, CodeDetailDTO.class);
            codeDetailDTO.setCodeProduct(code_detail.getProduct().getCode());
            codeDetailDTO.setNameProduct(code_detail.getProduct().getName());
            codeDetailDTOList.add(codeDetailDTO);

        });
        if (!DataUtil.isNullObject(branchTarget.getEmail())) {
            String sub = "THÔNG BÁO ĐIỀU CHUYỂN LIÊN CHI NHÁNH TỪ CHI NHÁNH " + checkBranch.toString();
            StringBuilder text = new StringBuilder();
            text.append("Chi nhánh " + checkBranch.toString()
                    + " thực hiện điều chuyển sang Chi nhánh " + idTarget.toString() + " với các sản phẩm: \n");
            for (CodeDetailDTO dto : codeDetailDTOList) {
                text.append(dto.getCodeProduct().toString() + "-" + dto.getNameProduct().toString() + "-" + dto.getQuantity().toString() + " \n");
            }
            text.append("Có thắc mắc gì vui lòng liên hệ với chi nhánh " + checkBranch.toString());
            this.emailService.sendEmail(branchTarget.getEmail(), sub, text.toString());
        }

        return ResponseUtil.ok(HttpStatus.OK);
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
    public List<ProductDTO> saveInventory(List<ProductDTO> dtoList) {
        List<ProductDTO> listMain = findByBranchUser();
        List<Products> list = MapperUtil.mapList(listMain, Products.class);
        for (ProductDTO productDTO : dtoList) {
            Optional<Products> products = list.stream()
                    .filter(productFilter -> Objects.equals(productFilter.getId(), productDTO.getId()))
                    .findAny();
            if (products.isEmpty()) {
                throw new BadRequestException("Sản phẩm không nằm trong chi nhánh");
            }
        }

        Code code = this.codeRepository.save(Code
                .builder()
                .branchFrom(dtoList.get(0).getIdBranch())
                .branchTo(dtoList.get(0).getIdBranch())
                .code(this.codeService.rentcode("PKK"))
                .type("Nhập điều chỉnh")
                .build());

        for (ProductDTO productDTO : dtoList) {
            Code_Detail code_detail = new Code_Detail();
            Products products = this.productsRepository.findById(productDTO.getId()).orElseThrow();
            code_detail.setQuantity(products.getQuantity());
            products.setQuantity(productDTO.getQuantityFrom());
            products.setNote(productDTO.getNote());
            this.productsRepository.save(products);
            code_detail.setCode(code);
            code_detail.setProduct(products);
            code_detail.setQuantityReal(productDTO.getQuantityFrom());
            this.codeDetailRepository.save(code_detail);
        }
        return dtoList;
    }

    @Override
    public List<Products> exportInventory() {
        List<Products> productsList = this.productsRepository.findByUpdatedDate();
        List<Products> list = new ArrayList<>();
        for (Products products : productsList) {
            if (DateUtil.sdtf.format(products.getUpdatedDate()).equals(DateUtil.sdtf.format(productsList.get(0).getUpdatedDate()))) {
                list.add(products);
            }
        }

        return list;
    }


    @Override
    public List<ProductDTO> findByBranch(Long idGroup) {
        CustomUserDetails users = (CustomUserDetails) this.userService.getInfoUser();
        Branch branch = this.branchRepository.findByIdAndDeleted(users.getUsers().getBranch().getId(), 0);
        Product_Groups product_groups = null;

        if (idGroup != null)
            product_groups = this.product_groupRepository.findById(idGroup).orElse(null);
        List<Products> productsList = this.productsRepository.findByGroupQuantity(product_groups, branch);

        return MapperUtil.mapList(productsList, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> findBySupplier(Long idSupplier) {
        Supplier supplier = null;
        if (idSupplier != null)
            supplier = this.supplierRepository.findById(idSupplier).orElse(null);
        List<Products> productsList = this.productsRepository.findSupplier(supplier);
        return MapperUtil.mapList(productsList, ProductDTO.class);
    }

    @Override
    public ResponseEntity<?> paySupplier(CodeDTO codeDTO, List<ProductDTO> productDTOList) {

        Code code = this.codeRepository.save(Code
                .builder()
                .note(codeDTO.getNote())
                .code(codeDTO.getCode())
                .branchFrom(codeDTO.getBranchTo())
                .branchTo(codeDTO.getBranchTo())
                .build());
        List<ProductDTO> listOld = findBySupplier(codeDTO.getIdSupplier());
        for (ProductDTO dto : productDTOList) {
            if (listOld.stream().noneMatch(productDTO -> productDTO.getId().equals(dto.getId()))) {
                throw new BadRequestException("Wrong ID");
            }
            Products product = this.productsRepository.findById(dto.getId()).orElseThrow();
            if (product.getQuantity() < dto.getQuantity()) {
                throw new BadRequestException("Too quantity");
            }
            if (codeDTO.getIdSupplier() != null) {
                Supplier supplier = supplierRepository.findById(codeDTO.getIdSupplier()).orElse(null);
                if (supplier == null) {
                    throw new BadRequestException("Supplier not found");
                }
                List<Supplier_Debit> suppliersDebit = supplier_debitRepository.findBySupplierAndDeleted(supplier, 0);
            }
            product.setQuantity(product.getQuantity() - dto.getQuantity());
            this.productsRepository.save(product);
            this.codeDetailRepository.save(Code_Detail
                    .builder()
                    .code(code)
                    .quantity(dto.getQuantity())
                    .quantityReal(product.getQuantity())
                    .product(product)
                    .build());
        }
        return ResponseUtil.ok(HttpStatus.OK);
    }

}
