package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.entity.Product_Groups;
import com.nextsol.khangbb.model.Product_GroupDTO;
import com.nextsol.khangbb.repository.Product_GroupRepository;
import com.nextsol.khangbb.repository.ProductsRepository;
import com.nextsol.khangbb.service.Product_GroupService;
import com.nextsol.khangbb.util.MapperUtil;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class Product_GroupServiceImp implements Product_GroupService {

    private final Product_GroupRepository product_groupRepository;

    private final ProductsRepository productsRepository;

    @Override
    public Page<Product_GroupDTO> findAll(Pageable pageable) {
        return MapperUtil.mapEntityPageIntoDtoPage(
                this.product_groupRepository.findByDeleted(0, pageable), Product_GroupDTO.class);
    }

    @Override
    public Optional<Product_GroupDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Product_GroupDTO create(Product_GroupDTO request) {
        return null;
    }

    @Override
    public ResponseEntity<?> save(Product_GroupDTO request) {
        Product_Groups check = product_groupRepository.findByDeletedAndCodeOrName(0, request.getCode(), request.getName()).orElse(null);
        if (check != null)
            return ResponseUtil.badRequest("Mã nhóm hoặc tên nhóm đã tồn tại");

        Product_Groups product_groups = MapperUtil.map(request, Product_Groups.class);

        product_groups.setDeleted(0);

        product_groups = product_groupRepository.save(product_groups);
        return ResponseUtil.ok(MapperUtil.map(product_groups, Product_GroupDTO.class));
    }

    @Override
    public ResponseEntity<?> update(Product_GroupDTO dto) {
        Product_Groups check = product_groupRepository.findByDeletedAndName(0, dto.getName()).orElse(null);
        if (check != null) {
            if (check.getId() != dto.getId())
                return ResponseUtil.badRequest("Tên nhóm đã tồn tại");
        }
        Product_Groups save = MapperUtil.map(dto, Product_Groups.class);
        save.setDeleted(0);
        this.product_groupRepository.save(save);
        return ResponseUtil.ok(dto);
    }

    @Override
    public List<Product_Groups> find(Integer deleted) {
        return product_groupRepository.findByDeleted(deleted);
    }

    @Override
    public void importToDb(MultipartFile multipartfiles) {
        try {
            XSSFWorkbook workBook = new XSSFWorkbook(multipartfiles.getInputStream());
            XSSFSheet sheet = workBook.getSheetAt(0);
            if (sheet.getLastRowNum() > 1) {
                List<Product_Groups> list = new ArrayList<>();
                List<Product_Groups> checkList = product_groupRepository.findAll();
                for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                    XSSFRow row = sheet.getRow(i);
                    if (row.getCell(0) == null
                            || row.getCell(1) == null
                            || row.getCell(0).getStringCellValue().isEmpty()
                            || row.getCell(1).getStringCellValue().isEmpty())
                        continue;
                    String name = row.getCell(0).getStringCellValue();
                    String code = row.getCell(1).getStringCellValue();
                    Product_Groups product_groups = Product_Groups.builder()
                            .name(name)
                            .code(code)
                            .deleted(0)
                            .actived(0)
                            .build();
                    Product_Groups check = checkList.stream().filter(x -> x.getCode().equals(product_groups.getCode())
                            && x.getName().equals(product_groups.getName())).findAny().orElse(null);
                    if (check == null && !list.contains(product_groups))
                        list.add(product_groups);
                }
                product_groupRepository.saveAllAndFlush(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Product_Groups productGroups = this.product_groupRepository.findById(id).orElseThrow();
        if (productsRepository.count() > 0)
            return ResponseUtil.badRequest("Không thể xóa nhóm khi đang còn sản phẩm trong nhóm");
        if (productGroups.getDeleted() == 1) return ResponseUtil.notFound(HttpStatus.NOT_FOUND);
        productGroups.setDeleted(1);
        this.product_groupRepository.save(productGroups);
        return ResponseUtil.ok(HttpStatus.OK);
    }

    @Override
    public Product_GroupDTO toProduct_GroupDTO(Product_Groups product_groups) {
        return null;
    }

    @Override
    public Optional<Product_Groups> findByCode(String code) {
        return this.product_groupRepository.findByCodeContaining(code);
    }
}
