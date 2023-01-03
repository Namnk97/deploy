package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.constant.Constant;
import com.nextsol.khangbb.entity.*;
import com.nextsol.khangbb.model.paymentSlipModel.*;
import com.nextsol.khangbb.repository.*;
import com.nextsol.khangbb.service.PaymentSlipService;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
@RequiredArgsConstructor
public class PaymentSlipServiceImp implements PaymentSlipService {
    private final Purchase_HistoryRepository purchase_historyRepository;
    private final CustomersRepository customersRepository;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final EndShiftRepository endShiftRepository;
    private final PayOrderSlipRepository payOrderSlipRepository;

    @Override
    public Page<Purchase_History> findAll(Pageable pageable, FilterRequest request) {
        if (request.getToDate() != null)
            request.setToDate(Date.from(request.getToDate().toInstant().plus(1, ChronoUnit.DAYS)));
        Customers customers = null;
        if (request.getCustomer() != null)
            customers = customersRepository.findById(request.getCustomer()).orElseThrow();
        return purchase_historyRepository.filter(
                pageable,
                getInfoUser().getBranch().getId(),
                request.getFromDate(),
                request.getToDate(),
                request.getReason(),
                customers,
                request.getBillCode()
        );
    }

    @Override
    public ResponseEntity<?> findAllDTO(Pageable pageable, FilterRequest request) {
        if (request.getToDate() != null)
            request.setToDate(Date.from(request.getToDate().toInstant().plus(1, ChronoUnit.DAYS)));
        Customers customers = null;
        if (request.getCustomer() != null)
            customers = customersRepository.findById(request.getCustomer()).orElseThrow();
        Page<PurchaseResponse> page = purchase_historyRepository.filterDTO(
                pageable,
                getInfoUser().getBranch().getId(),
                request.getFromDate(),
                request.getToDate(),
                request.getType(),
                request.getReason(),
                customers,
                request.getBillCode()
        );
        BigDecimal collection = BigDecimal.valueOf(0);
        BigDecimal spending = BigDecimal.valueOf(0);
        for (PurchaseResponse item : page) {
            if (item.getPrice() == null)
                continue;
            if (item.getType().equals(Constant.SPEND_TYPE))
                spending = spending.add(item.getPrice());
            else
                collection = collection.add(item.getPrice());
        }
        FilterDTO dto = new FilterDTO();
        dto.setPurchaseResponses(page);
        dto.setTotalCollection(collection);
        dto.setTotalSpending(spending);
        dto.setTotalPrice(collection.add(spending));
        return ResponseUtil.ok(dto);
    }

    @Override
    public ResponseEntity<?> getListOrderBill() {
        return ResponseEntity.ok(purchase_historyRepository.getListOrderBill(getInfoUser().getBranch().getId()));
    }

    @Override
    public ResponseEntity<?> save(SaveRequest request) {
        Customers customers = new Customers();
        if (request.getCustomer() != null)
            customers = customersRepository.findById(request.getCustomer()).orElse(null);
        Users users = getInfoUser();
        Purchase_History purchase_history = new Purchase_History();
        if (request.getReason().equals(Constant.GET_ORDER)) {
            if (request.getOrderDate() == null)
                return ResponseUtil.badRequest("Ngày đặt hàng không được để trống!");
            if (request.getReceivedDate() == null)
                return ResponseUtil.badRequest("Ngày nhận hàng không đc để trống!");
            purchase_history.setOrderDate(request.getOrderDate());
            purchase_history.setReceivedDate(request.getReceivedDate());
            purchase_history.setCustomer(customers);
        }
        PayOrderSlip payOrderSlip = new PayOrderSlip();
        if (request.getReason().equals(Constant.PAY_ORDER)) {
            if (request.getBillCode() == null)
                return ResponseUtil.badRequest("Phiếu đặt hàng không được để trống!");
            else {
                Bill bill = billRepository.findByCode(request.getBillCode());
                Purchase_History pay = purchase_historyRepository.findByBillAndStatus(bill, 0);
                if (pay != null) {
                    if (!pay.getCustomer().equals(customers))
                        return ResponseUtil.badRequest("Khách hàng phải trùng với khách hàng của phiếu thu!");
                    payOrderSlip.setOrderBillCode(pay.getBill().getCode());
                    pay.setStatus(1);// hoàn thành
                    purchase_historyRepository.save(pay);
                    purchase_history.setStatus(1);
                } else
                    return ResponseUtil.badRequest("Phiếu đặt hàng không hợp lệ!");
                purchase_history.setCustomer(customers);
            }
        }
        Bill bill = new Bill();
        bill.setCode(getBillCodeCount(Constant.TYPE_PAYMENT_SLIP.get(request.getReason())));
        purchase_history.setType(request.getType());
        purchase_history.setReason(request.getReason());
        if (request.getType().equals(Constant.SPEND_TYPE))
            purchase_history.setPrice(BigDecimal.valueOf(0).subtract(request.getPrice()));
        else
            purchase_history.setPrice(request.getPrice());
        purchase_history.setDescription(request.getDescription());
        purchase_history.setSeller(users.getUsername());
        purchase_history.setTypePayment(Constant.CASH_PAYMENT_TYPE);
        purchase_history.setBill(billRepository.save(bill));
        purchase_history.setBranchId(getInfoUser().getBranch().getId());
        purchase_history = purchase_historyRepository.save(purchase_history);
        payOrderSlip.setId(purchase_history.getId().toString());
        if (request.getReason().equals(Constant.PAY_ORDER))
            payOrderSlipRepository.save(payOrderSlip);
        return ResponseUtil.ok(purchase_historyRepository.filterDTOById(getInfoUser().getBranch().getId(),purchase_history.getId()));
    }

    @Override
    public ResponseEntity<?> update(UpdateRequest request) {
        Purchase_History purchase_history = purchase_historyRepository.findById(request.getId()).orElse(null);
        if (purchase_history == null)
            return ResponseUtil.badRequest("Sửa thất bại!");
        if (purchase_history.getStatus() == 2)
            return ResponseUtil.badRequest("Phiếu đã bị xóa!");
        if (purchase_history.getEndShift() != null)
            return ResponseUtil.badRequest("Sửa thất bại!");
        if (purchase_history.getReason().equals(Constant.GET_ORDER)) {
            if (request.getCustomer() == null)
                return ResponseUtil.badRequest("Khách hàng không hợp lệ!");
            Customers customers = customersRepository.findById(request.getCustomer()).orElse(null);
            purchase_history.setCustomer(customers);
            purchase_history.setOrderDate(request.getOrderDate());
            purchase_history.setReceivedDate(request.getReceivedDate());
        }
        purchase_history.setTotalPrice(request.getPrice());
        Users users = userRepository.findByUsernameAndDeleted(request.getSeller(), 0).orElse(null);
        if (users == null)
            return ResponseUtil.badRequest("Nhân viên bán hàng không hợp lệ!");
        purchase_history.setSeller(request.getSeller());
        if (request.getDescription() == null)
            return ResponseUtil.badRequest("Mổ tả không được để trống!");
        purchase_history.setDescription(request.getDescription());
        purchase_historyRepository.save(purchase_history);
        return ResponseUtil.ok("Sửa thành công");
    }

    @Override
    public ResponseEntity<?> remove(Long id) {
        Purchase_History purchase_history = purchase_historyRepository.findById(id).orElse(null);
        if (purchase_history != null) {
            if (purchase_history.getStatus() == 0) {
                purchase_history.setStatus(2);// đã bị xóa
                purchase_history.setEndShift((long)0);
            } else if (purchase_history.getStatus() == 1)
                return ResponseUtil.badRequest("Không thể xóa phiếu đã được thanh toán!");
            else
                return ResponseUtil.badRequest("Phiếu này đã bị xóa!");
            purchase_historyRepository.saveAndFlush(purchase_history);
            return ResponseUtil.ok("Xóa thành công!");
        }
        return ResponseUtil.badRequest("Xóa không thành công!");
    }

    @Override
    public ResponseEntity<?> statisticalEndShift() {
        EndShift endShift = endShiftRepository.findFirstByOrderByEndDateDesc();
        List<Purchase_History> list = purchase_historyRepository.findByEndShiftIsNullAndEndLastShiftAndBranchId( 0,getInfoUser().getBranch().getId());
        list.addAll(purchase_historyRepository.findByEndShiftIsNullAndEndLastShiftAndBranchId( null,getInfoUser().getBranch().getId()));
        BigDecimal cash = BigDecimal.valueOf(0);
        if (endShift != null)
            cash = endShift.getAfterPrice() == null ? BigDecimal.valueOf(0) : BigDecimal.valueOf(endShift.getAfterPrice());
        BigDecimal transfer = BigDecimal.valueOf(0);
        BigDecimal VNPay = BigDecimal.valueOf(0);
        BigDecimal card = BigDecimal.valueOf(0);
        BigDecimal debit = BigDecimal.valueOf(0);
        for (Purchase_History item : list) {
            if (item.getTypePayment() == null || item.getPrice() == null)
                continue;
            if (item.getTypePayment().equals(Constant.CASH_PAYMENT_TYPE))
                cash = cash.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.TRANSFER_PAYMENT_TYPE))
                transfer = transfer.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.VNPAY_PAYMENT_TYPE))
                VNPay = VNPay.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.CARD_PAYMENT_TYPE))
                card = card.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.DEBIT_PAYMENT_TYPE))
                debit = debit.add(item.getPrice());
        }
        Map<String,BigDecimal> map = new HashMap<>();
        map.put("price",cash);
        map.put("transfer",transfer);
        map.put("VNPay",VNPay);
        map.put("card",card);
        map.put("debit",debit);
        return ResponseUtil.ok(map);
    }

    @Override
    public ResponseEntity<?> endShift(Integer accept, BigDecimal price) {
        Users users = getInfoUser();
        List<Purchase_History> list = purchase_historyRepository.findByEndShiftIsNullAndSellerAndEndLastShiftAndBranchId(users.getUsername(), 0,getInfoUser().getBranch().getId());
        BigDecimal cash = BigDecimal.valueOf(0);
        BigDecimal transfer = BigDecimal.valueOf(0);
        BigDecimal VNPay = BigDecimal.valueOf(0);
        BigDecimal card = BigDecimal.valueOf(0);
        BigDecimal debit = BigDecimal.valueOf(0);
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        EndShift endShift = endShiftRepository.findFirstByOrderByEndDateDesc();
        EndShift saveEnd = new EndShift();
        if (endShift != null) {
            saveEnd.setStartDate(endShift.getEndDate());
            saveEnd.setBeforePrice(endShift.getAfterPrice());
            saveEnd.setBeforePrice(endShift.getAfterPrice());
        }
        saveEnd.setEndDate(new Date());
        if (accept == 1) {
            saveEnd.setAfterPrice((double) 0);
        } else if (endShift != null)
            saveEnd.setAfterPrice(endShift.getAfterPrice());
        saveEnd = endShiftRepository.save(saveEnd);
        for (Purchase_History item : list) {
            item.setEndShift(saveEnd.getId());
            if (item.getTypePayment() == null || item.getPrice() == null)
                continue;
            if (item.getTypePayment().equals(Constant.CASH_PAYMENT_TYPE))
                cash = cash.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.TRANSFER_PAYMENT_TYPE))
                transfer = transfer.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.VNPAY_PAYMENT_TYPE))
                VNPay = VNPay.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.CARD_PAYMENT_TYPE))
                card = card.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.DEBIT_PAYMENT_TYPE))
                debit = debit.add(item.getPrice());
            if (accept == 0)
                item.setEndLastShift(1);
            totalPrice = totalPrice.add(item.getPrice());
        }
        saveEnd.setCash(cash);
        saveEnd.setTransfer(transfer);
        saveEnd.setVNPay(VNPay);
        saveEnd.setCard(card);
        saveEnd.setDebit(debit);
        Purchase_History purchase_history = new Purchase_History();
        Bill bill = new Bill();
        bill.setCode(getBillCodeCount(Constant.TYPE_REASON[1]));
        purchase_history.setTypePayment(Constant.CASH_PAYMENT_TYPE);
        purchase_history.setType(Constant.SPEND_TYPE);
        purchase_history.setReason(Constant.SPEND_END_SHIFT);
        if (accept == 1 && price != null) {
            purchase_history.setPrice(price);
            saveEnd.setAfterPrice(totalPrice.subtract(price).doubleValue());
        } else {
            purchase_history.setPrice(totalPrice);
            purchase_history.setEndLastShift(1);
            saveEnd.setAfterPrice((double) 0);
        }
        purchase_history.setStatus(1);
        purchase_history.setSeller(users.getUsername());
        saveEnd = endShiftRepository.save(saveEnd);
        purchase_history.setEndShift(saveEnd.getId());
        purchase_history.setBranchId(getInfoUser().getBranch().getId());
        list.add(purchase_history);
        purchase_history.setBill(billRepository.save(bill));
        purchase_historyRepository.saveAllAndFlush(list);
        return ResponseUtil.ok("Kết ca thành công");
    }

    @Override
    public ResponseEntity<?> endShiftFilter(EndShiftFilterRequest request, Pageable pageable) {
        if (request.getFromDate() != null)
            request.setFromDate(Date.from(request.getFromDate().toInstant().minus(1, ChronoUnit.DAYS)));
        if (request.getToDate() != null)
            request.setToDate(Date.from(request.getToDate().toInstant().plus(1, ChronoUnit.DAYS)));
        return ResponseUtil.ok(purchase_historyRepository.filterEndShift(
                pageable,
                getInfoUser().getBranch().getId(),
                request.getUser(),
                request.getFromDate(),
                request.getToDate())
        );
    }

    @Override
    public ResponseEntity<?> endShiftDetail(Long id, Pageable pageable) {
        EndShift endShift = endShiftRepository.findById(id).orElse(null);
        if (endShift == null)
            return ResponseUtil.noContent(null);
        Map<String, Object> map = getEndShiftResponse(purchase_historyRepository.endShiftDetail(pageable, id));
        map.put("endShift", endShift);
        endShift = endShiftRepository.findById(id - 1).orElse(null);
        map.put("endShiftBefore", endShift);
        return ResponseUtil.ok(map);
    }

    @Override
    public ResponseEntity<?> getEndShiftList(Pageable pageable) {
        Users users = getInfoUser();
        Map<String, Object> map = new HashMap<>();
        map.put("currentShift", getEndShiftResponse(purchase_historyRepository.endShiftList(pageable, getInfoUser().getBranch().getId(), users.getUsername())));
        map.put("beforeShift", endShiftRepository.findFirstByOrderByEndDateDesc());
        return ResponseUtil.ok(map);
    }

    @Override
    public ResponseEntity<?> adjustSafes(BigDecimal editPrice) {
        Users users = getInfoUser();
        List<Purchase_History> list = purchase_historyRepository.findByEndShiftIsNullAndSellerAndEndLastShiftAndBranchId(users.getUsername(), 0,getInfoUser().getBranch().getId());
        list.addAll(purchase_historyRepository.findByEndShiftIsNullAndEndLastShiftAndBranchId( null,getInfoUser().getBranch().getId()));
        BigDecimal price = BigDecimal.valueOf(0);
        for (Purchase_History item : list) {
            if (item.getPrice() == null && item.getTypePayment().equals(Constant.CASH_PAYMENT_TYPE))
                continue;
            price = price.add(item.getPrice());
        }
        Purchase_History purchase_history = new Purchase_History();
        Bill bill = new Bill();
        if (editPrice.compareTo(price) < 0) {
            purchase_history.setType(Constant.SPEND_TYPE);
            purchase_history.setReason(Constant.SPEND_ADJUST_SAFE);
            bill.setCode(getBillCodeCount(Constant.TYPE_REASON[0]));
        } else {
            purchase_history.setType(Constant.COLLECT_TYPE);
            purchase_history.setReason(Constant.COLLECT_ADJUST_SAFE);
            bill.setCode(getBillCodeCount(Constant.TYPE_REASON[5]));
        }
        purchase_history.setTypePayment(Constant.CASH_PAYMENT_TYPE);
        purchase_history.setBranchId(users.getBranch().getId());
        purchase_history.setPrice(editPrice.subtract(price));
        purchase_history.setStatus(1);
        purchase_history.setSeller(users.getUsername());
        purchase_history.setDescription(null);
        purchase_history.setBill(billRepository.save(bill));
        purchase_historyRepository.save(purchase_history);
        return ResponseUtil.ok("Điều chỉnh tiền trong két thành công");
    }

    public Map<String, Object> getEndShiftResponse(Page<PurchaseResponse> list) {
        BigDecimal cash = BigDecimal.valueOf(0);
        BigDecimal transfer = BigDecimal.valueOf(0);
        BigDecimal VNPay = BigDecimal.valueOf(0);
        BigDecimal card = BigDecimal.valueOf(0);
        BigDecimal debit = BigDecimal.valueOf(0);
        for (PurchaseResponse item : list) {
            if (item.getTypePayment() == null || item.getPrice() == null)
                continue;
            if (item.getTypePayment().equals(Constant.CASH_PAYMENT_TYPE))
                cash = cash.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.TRANSFER_PAYMENT_TYPE))
                transfer = transfer.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.VNPAY_PAYMENT_TYPE))
                VNPay = VNPay.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.CARD_PAYMENT_TYPE))
                card = card.add(item.getPrice());
            if (item.getTypePayment().equals(Constant.DEBIT_PAYMENT_TYPE))
                debit = debit.add(item.getPrice());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("cash", cash);
        response.put("transfer", transfer);
        response.put("VNPay", VNPay);
        response.put("card", card);
        response.put("debit", debit);
        response.put("endShiftList", list);
        return response;
    }

    @Override
    public ResponseEntity<?> editBillDetail(UpdateRequest request) {
        Purchase_History purchase_history = purchase_historyRepository.findById(request.getId()).orElse(null);
        if (purchase_history == null)
            return ResponseUtil.badRequest("Lưu thông tin thất bại!");
        if (purchase_history.getStatus() == 2)
            return ResponseUtil.badRequest("Không thể sửa phiếu đã bị xóa");
        if (request.getCustomer() != null)
            customersRepository.findById(request.getCustomer()).ifPresent(purchase_history::setCustomer);
        else
            purchase_history.setCustomer(null);
        purchase_history.setSeller(request.getSeller());
        purchase_history.setDescription(request.getDescription());
        purchase_historyRepository.save(purchase_history);
        return ResponseUtil.ok("Lưu thông tin thành công");
    }

    @Override
    public ResponseEntity<?> printfBill(String code) {
        switch (code) {
            case "Thu tiền bán lẻ":
            case "Thu tiền bán sỉ":
            case "Nhập trả hàng":
                Map<String, Object> map = new HashMap<>();
                map.put("", purchase_historyRepository.printfBill(code));
                return ResponseUtil.ok(map);
            default:
                return null;
        }
    }

    @Override
    public Users getInfoUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(null);
    }

    public String getBillCodeCount(String type) {
        Users users = getInfoUser();
        List<Long> ids = billRepository.findFirstIdByType(type);
        if (ids.isEmpty())
            return type + "." + users.getBranch().getId() + ".1";
        //
        Long id = ids.get(0);
        Bill bill = billRepository.findById(id).orElse(null);
        assert bill != null;
        String count = bill.getCode().split("\\.")[2];
        int raw = Integer.parseInt(count);
        raw++;
        return type + "." + users.getBranch().getId() + "." + raw;
    }
}