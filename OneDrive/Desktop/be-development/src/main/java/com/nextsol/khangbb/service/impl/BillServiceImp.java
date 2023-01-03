package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.constant.Constant;
import com.nextsol.khangbb.entity.*;
import com.nextsol.khangbb.exception.BadRequestException;
import com.nextsol.khangbb.model.Bill_BillDTO;
import com.nextsol.khangbb.model.Customer.CustomerResponse;
import com.nextsol.khangbb.model.ProductDTO;
import com.nextsol.khangbb.model.PurchaseHistoryDTO;
import com.nextsol.khangbb.model.paymentSlipModel.BillDTO;
import com.nextsol.khangbb.model.paymentSlipModel.ProductBillResponse;
import com.nextsol.khangbb.model.paymentSlipModel.PurchaseResponse;
import com.nextsol.khangbb.repository.*;
import com.nextsol.khangbb.security.CustomUserDetails;
import com.nextsol.khangbb.service.BillService;
import com.nextsol.khangbb.service.UserService;
import com.nextsol.khangbb.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BillServiceImp implements BillService {
    private final BillRepository billRepository;
    private final ProductsRepository productsRepository;
    private final Bill_DetailRepository bill_detailRepository;
    private final Purchase_HistoryRepository purchase_historyRepository;
    private final CustomersRepository customersRepository;
    private final UserService userService;
    private final PayOrderSlipRepository payOrderSlipRepository;

    private final UserRepository userRepository;


    @Override
    public Page<Bill> getAll(Pageable pageable) {
        return billRepository.findAll(pageable);
    }

    @Override
    public BillDTO detail(Long id) {
        BillDTO dto = new BillDTO();
        PurchaseResponse purchaseResponse = purchase_historyRepository.filterDTOById(getInfoUser().getBranch().getId(), id);
        dto.setBill(purchaseResponse);
        CustomerResponse customerResponse = purchase_historyRepository.findCustomer(id).orElse(null);
        if (customerResponse != null) {
            dto.setCustomerId(customerResponse.getId());
            dto.setCustomerName(customerResponse.getNameAndPhone());
        }
        if (purchaseResponse == null)
            return dto;
        Bill bill = billRepository.findByCode(purchaseResponse.getBillCode());
        boolean check = false;
        if (purchaseResponse.getReason().equals(Constant.PAY_ORDER)) {
            PayOrderSlip payOrderSlip = payOrderSlipRepository.findById(id.toString()).orElse(null);
            assert payOrderSlip != null;
            Long orderBillId = purchase_historyRepository.findIdByCode(payOrderSlip.getOrderBillCode());
            dto.setOrderBillId(orderBillId);
            bill = billRepository.findByCode(payOrderSlip.getOrderBillCode());
            check = true;
        }
        if (bill == null)
            return dto;
        List<Bill_Detail> list = bill_detailRepository.findByBillId(bill.getId());
        if (list == null)
            return dto;
        List<ProductBillResponse> listProduct = new ArrayList<>();
        for (Bill_Detail item : list) {
            if (item == null || item.getProducts() == null)
                continue;
            Products products = item.getProducts();
            ProductBillResponse response = new ProductBillResponse();
            response.setId(products.getId());
            response.setName(products.getName());
            response.setQuantity(item.getQuantity());
            response.setImportPrice(check ? products.getImportPrice() : (BigDecimal.valueOf(0).subtract(products.getImportPrice())));
            response.setRetail_price(products.getRetail_price());
            response.setWhole_price(products.getWhole_price());
            response.setRetail_discount_money(products.getRetail_discount_money());
            response.setWholesale_discount_money(products.getWholesale_discount_money());
            response.setRetail_discount(products.getRetail_discount());
            response.setWholesale_discount(products.getWholesale_discount());
            response.setDiscount(item.getDiscount());
            response.setDiscountMoney(item.getDiscountMoney());
            response.setDescription(item.getDescription());
            listProduct.add(response);
        }
        dto.setProductList(listProduct);
        return dto;
    }

    @Override
    public Bill_BillDTO payMoney(Bill_BillDTO billDTO, List<ProductDTO> productDTOList, PurchaseHistoryDTO purchaseDTO, String phone) {
        CustomUserDetails customUserDetails = (CustomUserDetails) this.userService.getInfoUser();
        String action = purchaseDTO.getReason().equals("Thu tiền bán lẻ") ? "TBL" : "TBS";
        Bill bill = this.billRepository.save(Bill
                .builder()
                .code(rentcode(action))
                .billDetail(new ArrayList<>())
                .discountMoney(billDTO.getDiscountMoney())
                .discount(billDTO.getDiscount())
                .build()
        );

        BigDecimal price = new BigDecimal(0);
        List<Products> lstProduct = productsRepository.findByIdIn
                (productDTOList.stream().map(ProductDTO::getId).collect(Collectors.toList()));

        //check quantity
        boolean checkQuantity = checkQuantity(lstProduct, productDTOList);
        if (!checkQuantity) {
            throw new BadRequestException("Không đủ số lượng");
        }

        for (ProductDTO productDTO : productDTOList) {
            Products products = lstProduct.stream()
                    .filter(productFilter -> Objects.equals(productFilter.getId(), productDTO.getId())).findAny().get();
            Bill_Detail bill_detail = Bill_Detail
                    .builder()
                    .bill(bill)
                    .discountMoney(productDTO.getDiscountMoney())
                    .discount(productDTO.getDiscount())
                    .products(products)
                    .quantity(productDTO.getQuantity())
                    .quantityReal(productDTO.getQuantity())
                    .description(productDTO.getDescription())
                    .build();
            bill.getBillDetail().add(bill_detail);
            if (purchaseDTO.getReason().equals("Thu tiền bán lẻ")) {
                purchaseDTO.setPrice(products.getRetail_price().multiply(BigDecimal.valueOf(productDTO.getQuantity())).subtract(productDTO.getDiscountMoney().multiply(BigDecimal.valueOf(productDTO.getQuantity()))));
            } else {
                purchaseDTO.setPrice(products.getWhole_price().multiply(BigDecimal.valueOf(productDTO.getQuantity())).subtract(productDTO.getDiscountMoney().multiply(BigDecimal.valueOf(productDTO.getQuantity()))));
            }
            price = price.add(purchaseDTO.getPrice());
            products.setQuantity(products.getQuantity() - productDTO.getQuantity());
        }
        this.productsRepository.saveAll(lstProduct);
        if (price.compareTo(billDTO.getDiscountMoney()) > 0) {
            price = price.subtract(billDTO.getDiscountMoney());
        } else {
            throw new BadRequestException("Chiết khấu tiền sai ");
        }
        this.billRepository.save(bill);
        Purchase_History purchase_history = Purchase_History
                .builder()
                .bill(bill)
                .type("Thu Tiền")
                .reason(purchaseDTO.getReason())
                .price(price)
                .typePayment(purchaseDTO.getTypePayment())
                .seller(customUserDetails.getUsers().getUsername())
                .description(purchaseDTO.getDescription())
                .status(1)
                .branchId(customUserDetails.getUsers().getBranch().getId())
                .totalPrice(purchaseDTO.getTotalPrice())
                .endLastShift(0)
                .build();
        if (purchaseDTO.getTypePayment().equalsIgnoreCase("Ghi nợ")) {
            Customers customers = this.customersRepository.findByPhone(phone);
            purchase_history.setStatus(0);
            purchase_history.setCustomer(customers);
            this.purchase_historyRepository.save(purchase_history);
            if (purchaseDTO.getTotalPrice().subtract(price).compareTo(BigDecimal.valueOf(0)) < 0) {
                customers.setDebit(customers.getDebit().subtract(purchaseDTO.getTotalPrice().subtract(price)));
                this.customersRepository.save(customers);
            }

        }
        if (this.customersRepository.findByPhone(phone) == null) {
            this.purchase_historyRepository.save(purchase_history);

        } else {
            Customers customers = this.customersRepository.findByPhone(phone);
            purchase_history.setCustomer(customers);
            this.purchase_historyRepository.save(purchase_history);
        }
        return MapperUtil.map(bill, Bill_BillDTO.class);
    }

    @Override
    public List<BillDTO> codeBill(String codeBill) {
        List<Bill> billList = this.billRepository.findByCodeContaining(codeBill);
        List<BillDTO> billDTOList = MapperUtil.mapList(billList, BillDTO.class);
        return billDTOList;
    }


    public String rentcode(String type) {
        Users users = getInfoUser();
        List<Long> ids = this.billRepository.findFirstIdByType(type);
        if (ids.isEmpty())
            return type + "." + users.getBranch().getId() + ".1";
        //
        Long id = ids.get(0);
        Bill bill = this.billRepository.findById(id).orElse(null);
        assert bill != null;
        String count = bill.getCode().split("\\.")[2];
        int raw = Integer.parseInt(count);
        raw++;
        return type + "." + users.getBranch().getId() + "." + raw;
    }

    public Users getInfoUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(null);
    }

    private boolean checkQuantity(List<Products> lstProduct, List<ProductDTO> productDTOList) {
        for (ProductDTO productDTO : productDTOList) {
            Optional<Products> products = lstProduct.stream()
                    .filter(productFilter -> Objects.equals(productFilter.getId(), productDTO.getId())
                            && productFilter.getQuantity() >= productDTO.getQuantity())
                    .findAny();

            if (products.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
