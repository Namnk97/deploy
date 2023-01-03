package com.nextsol.khangbb.service.impl;


import com.nextsol.khangbb.entity.Branch;
import com.nextsol.khangbb.entity.Code;
import com.nextsol.khangbb.entity.Code_Detail;
import com.nextsol.khangbb.entity.Products;
import com.nextsol.khangbb.model.CodeDetailDTO;
import com.nextsol.khangbb.repository.BranchRepository;
import com.nextsol.khangbb.repository.CodeDetailRepository;
import com.nextsol.khangbb.repository.CodeRepository;
import com.nextsol.khangbb.repository.ProductsRepository;
import com.nextsol.khangbb.service.CodeDetailService;
import com.nextsol.khangbb.util.MapperUtil;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CodeDetailImp implements CodeDetailService {

    private final CodeDetailRepository codeDetailRepository;

    private final CodeRepository codeRepository;

    private final ProductsRepository productsRepository;

    private final BranchRepository branchRepository;

    @Override
    public List<CodeDetailDTO> displayDetail(Long idCode) {
        Code code = this.codeRepository.findById(idCode).orElseThrow();
        List<Code_Detail> detail = this.codeDetailRepository.findByCode(code);
        detail.forEach(code_detail -> {
            Products products = this.productsRepository.findById(code_detail.getProduct().getId()).orElseThrow();
            CodeDetailDTO codeDetailDTO = MapperUtil.map(code_detail, CodeDetailDTO.class);
            codeDetailDTO.setCodeProduct(products.getCode());
            codeDetailDTO.setNameProduct(products.getName());
        });
        List<CodeDetailDTO> list = MapperUtil.mapList(detail, CodeDetailDTO.class);
        return list;
    }

    @Override
    public ResponseEntity<?> saveProductLCN(Long idCode) {
        List<CodeDetailDTO> list = displayDetail(idCode);
        list.forEach(detailDTO -> {
            Products productMain = this.productsRepository.findById(detailDTO.getProduct().getId()).orElseThrow();
            Code code = this.codeRepository.findById(detailDTO.getIdCode()).orElseThrow();
            Branch branchTarget = this.branchRepository.findById(code.getBranchTo()).orElseThrow();
            Products productTarget = this.productsRepository.findByNameAndCodeAndBranch(detailDTO.getNameProduct(), detailDTO.getCodeProduct(), branchTarget)
                    .orElse(Products.builder()
                            .productGroup(productMain.getProductGroup())
                            .wholesale_discount(productMain.getWholesale_discount())
                            .retail_price(productMain.getRetail_price())
                            .wholesale_discount_money(productMain.getWholesale_discount_money())
                            .note("")
                            .unit(productMain.getUnit())
                            .whole_price(productMain.getWhole_price())
                            .retail_discount_money(productMain.getRetail_discount_money())
                            .retail_discount(productMain.getRetail_discount())
                            .quantity(0)
                            .importPrice(productMain.getImportPrice())
                            .code(productMain.getCode())
                            .deleted(0)
                            .actived(0)
                            .description(productMain.getDescription())
                            .branch(branchTarget)
                            .name(productMain.getName())
                            .build());
            productTarget.setQuantity(productTarget.getQuantity() + detailDTO.getQuantity());
//            productTarget.setNote(productTarget.getNote() + productDTO.getNote());
            this.productsRepository.save(productTarget);
            productMain.setQuantity(productMain.getQuantity() - detailDTO.getQuantity());
            this.productsRepository.save(productMain);
            code.setStatus(1);
            this.codeRepository.save(code);
        });
        return ResponseUtil.ok(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> rejectProductLCN(Long idCode, String reason) {
        List<CodeDetailDTO> list = displayDetail(idCode);
        list.forEach(detailDTO -> {
            Code code = this.codeRepository.findById(detailDTO.getIdCode()).orElseThrow();
            code.setStatus(2);
            code.setReason(reason);
            this.codeRepository.save(code);
        });
        return ResponseUtil.ok(HttpStatus.OK);
    }

}
