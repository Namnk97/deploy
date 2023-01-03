package com.nextsol.khangbb.service.impl;


import com.nextsol.khangbb.constant.Constant;
import com.nextsol.khangbb.entity.*;
import com.nextsol.khangbb.exception.BadRequestException;
import com.nextsol.khangbb.model.Bill_DetailDTO;
import com.nextsol.khangbb.model.CodeDTO;
import com.nextsol.khangbb.repository.*;
import com.nextsol.khangbb.service.BillDetailService;
import com.nextsol.khangbb.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BillDeatilServiceImp implements BillDetailService {

    private final BillRepository billRepository;

    private final Bill_DetailRepository bill_detailRepository;

    private final ProductsRepository productsRepository;

    private final CodeRepository codeRepository;

    private final CodeDetailRepository codeDetailRepository;

    private final Purchase_HistoryRepository historyRepository;

    private final CustomersRepository customersRepository;

    @Override
    public List<Bill_DetailDTO> findByBill(String codeBill) {
        Bill bill = this.billRepository.findByCode(codeBill);
        Purchase_History history = this.historyRepository.findByBillAndStatus(bill, 0);
        List<Bill_Detail> detailList = this.bill_detailRepository.findByBill(bill);
        List<Bill_DetailDTO> dtoList = new ArrayList<>();
        for (Bill_Detail billDetail : detailList) {
            Bill_DetailDTO dto = MapperUtil.map(billDetail, Bill_DetailDTO.class);
            Products products = this.productsRepository.findById(billDetail.getProducts().getId()).orElseThrow();
            dto.setNameProduct(products.getName());
            dto.setCodeProduct(products.getCode());
            dto.setIdProduct(products.getId());
            dto.setDiscountBill(bill.getDiscount());
            dto.setDiscountMoneyBill(bill.getDiscountMoney());
            if (history == null) {
            } else {
                dto.setNameCustomer(history.getCustomer().getFullname());
            }
            if (bill.getCode().split("\\.")[0].equalsIgnoreCase("TBL")) {
                dto.setPrice(products.getRetail_price());
                dto.setTotalPrice(products.getRetail_price()
                        .multiply(BigDecimal.valueOf(billDetail.getQuantity()))
                        .subtract(billDetail.getDiscountMoney()
                                .multiply(BigDecimal.valueOf(billDetail.getQuantity()))));
            }
            if (bill.getCode().split("\\.")[0].equalsIgnoreCase("TBS")) {
                dto.setPrice(products.getWhole_price());
                dto.setTotalPrice(products.getWhole_price()
                        .multiply(BigDecimal.valueOf(billDetail.getQuantity()))
                        .subtract(billDetail.getDiscountMoney()
                                .multiply(BigDecimal.valueOf(billDetail.getQuantity()))));
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<Bill_DetailDTO> payProduct(CodeDTO codeDTO, List<Bill_DetailDTO> detailList) {
        List<Bill_DetailDTO> detailDTOList = findByBill(codeDTO.getCodeBill());
        Code code = this.codeRepository.save(Code
                .builder()
                .codeBill(codeDTO.getCodeBill())
                .code(codeDTO.getCode())
                .note(codeDTO.getNote())
                .type("Nhập trả hàng")
                .build());
        for (Bill_DetailDTO dto : detailList) {
            if (detailDTOList.stream().noneMatch(bill_detailDTO -> bill_detailDTO.getId().equals(dto.getId()))) {
                throw new BadRequestException("Wrong ID");
            }
            Bill_Detail billDetail = this.bill_detailRepository.findById(dto.getId()).orElseThrow();
            if (billDetail.getQuantityReal() < dto.getQuantityPay()) {
                throw new BadRequestException("Too quantity");
            }
            Bill bill = this.billRepository.findById(billDetail.getBill().getId()).orElseThrow();
            Purchase_History history = this.historyRepository.findByBillAndStatus(bill, 0);
            Products products = this.productsRepository.findById(billDetail.getProducts().getId()).orElseThrow();
            if (history != null) {
                if (history.getTypePayment().equalsIgnoreCase("Ghi nợ")) {
                    if (bill.getCode().split("\\.")[0].equalsIgnoreCase("TBL")) {
                        BigDecimal pricePay = products.getRetail_price().multiply(BigDecimal.valueOf(dto.getQuantityPay()));
                        BigDecimal priceDiscout = (billDetail.getDiscountMoney().divide(BigDecimal.valueOf(billDetail.getQuantity())).multiply(BigDecimal.valueOf(dto.getQuantityPay())));
                        history.setPrice(history.getPrice().subtract(pricePay.subtract(priceDiscout)));
                        this.historyRepository.save(history);
                        Customers customers = this.customersRepository.findById(history.getCustomer().getId()).orElseThrow();
                        customers.setDebit(history.getPrice());
                        this.customersRepository.save(customers);
                    }
                    if (bill.getCode().split("\\.")[0].equalsIgnoreCase("TBS")) {
                        BigDecimal pricePay = products.getWhole_price().multiply(BigDecimal.valueOf(dto.getQuantityPay()));
                        BigDecimal priceDiscout = (billDetail.getDiscountMoney().divide(BigDecimal.valueOf(billDetail.getQuantity())).multiply(BigDecimal.valueOf(dto.getQuantityPay())));
                        history.setPrice(history.getPrice().subtract(pricePay.subtract(priceDiscout)));
                        this.historyRepository.save(history);
                        Customers customers = this.customersRepository.findById(history.getCustomer().getId()).orElseThrow();
                        customers.setDebit(history.getPrice());
                        this.customersRepository.save(customers);
                    }
                }
            }
            billDetail.setQuantityReal(billDetail.getQuantityReal() - dto.getQuantityPay());
            this.bill_detailRepository.save(billDetail);
            products.setQuantity(products.getQuantity() + dto.getQuantityPay());
            this.productsRepository.save(products);
            this.codeDetailRepository.save(Code_Detail
                    .builder()
                    .product(products)
                    .quantity(dto.getQuantityPay())
                    .code(code)
                    .build());
        }
        return detailDTOList;
    }
}
