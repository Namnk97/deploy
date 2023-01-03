package com.nextsol.khangbb.service.impl;


import com.nextsol.khangbb.entity.Bill;
import com.nextsol.khangbb.entity.Customers;
import com.nextsol.khangbb.entity.Purchase_History;
import com.nextsol.khangbb.exception.BadRequestException;
import com.nextsol.khangbb.model.PurchaseHistoryDTO;
import com.nextsol.khangbb.repository.BillRepository;
import com.nextsol.khangbb.repository.Bill_DetailRepository;
import com.nextsol.khangbb.repository.CustomersRepository;
import com.nextsol.khangbb.repository.Purchase_HistoryRepository;
import com.nextsol.khangbb.service.PurchaseHistoryService;
import com.nextsol.khangbb.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService {
    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private Purchase_HistoryRepository purchase_historyRepository;

    @Override
    public List<PurchaseHistoryDTO> displayDebitCustomer(Long idCustomer) {
        Customers customers = this.customersRepository.findById(idCustomer).orElseThrow();
        List<PurchaseHistoryDTO> historyListDTO = new ArrayList<>();
        List<Purchase_History> historyList = this.purchase_historyRepository.findByCustomerAndStatus(customers, 0);
        historyList.forEach(history -> {
            if (history.getTypePayment().equalsIgnoreCase("Ghi nợ")) {
                PurchaseHistoryDTO historyDTO = MapperUtil.map(history, PurchaseHistoryDTO.class);
                Bill bill = this.billRepository.findById(historyDTO.getIdBill()).orElseThrow();
                historyDTO.setCard(bill.getCode());
                historyDTO.setNameCustomer(customers.getFullname());
                historyDTO.setTotalPrice(customers.getDebit());
                historyListDTO.add(historyDTO);

            }
        });

        return historyListDTO;
    }

    @Override
    public List<PurchaseHistoryDTO> displayRequirement(Long idCustomer, Integer check) {
        List<PurchaseHistoryDTO> dtoList = this.displayDebitCustomer(idCustomer);
        if (check == 1) {//return 1 for FE : sort by Date
            List<PurchaseHistoryDTO> dtoListNew = dtoList
                    .stream()
                    .sorted(Comparator.comparing(PurchaseHistoryDTO::getCreatedDate))
                    .collect(Collectors.toList());
            return dtoListNew;
        } else {
            List<PurchaseHistoryDTO> dtoListNew = dtoList
                    .stream()
                    .sorted(Comparator.comparing(PurchaseHistoryDTO::getIdBill))
                    .collect(Collectors.toList());
            return dtoListNew;
        }

    }

    @Override
    public List<PurchaseHistoryDTO> paymentDebitCustomer(Long idCustomer, BigDecimal payDebit) {
        List<PurchaseHistoryDTO> dtoList = this.displayDebitCustomer(idCustomer);
        for (PurchaseHistoryDTO dtoNew : dtoList) {
            Customers customers = this.customersRepository.findById(idCustomer).orElseThrow();
            if (payDebit.compareTo(customers.getDebit()) > 0) {
                throw new BadRequestException("Too money");
            }
            payDebit = payDebit.subtract(dtoNew.getPrice());
            Purchase_History history = MapperUtil.map(dtoNew, Purchase_History.class);
            if (payDebit.compareTo(BigDecimal.valueOf(0)) == 0) {
                history.setTotalPrice(dtoNew.getPrice());
                history.setPrice(payDebit);
                history.setStatus(1);

                history.setCustomer(customers);
                this.purchase_historyRepository.save(history);
                customers.setDebit(customers.getDebit().subtract(history.getTotalPrice()));
                this.customersRepository.save(customers);
                break;
            }
            if (payDebit.compareTo(BigDecimal.valueOf(0)) < 0) {
                history.setTotalPrice(history.getPrice().add(payDebit));
                history.setPrice(history.getPrice().subtract(history.getTotalPrice()));
                history.setStatus(1);
                history.setCustomer(customers);
                this.purchase_historyRepository.save(history);
                customers.setDebit(customers.getDebit().subtract(history.getTotalPrice()));
                Bill bill = this.billRepository.findById(dtoNew.getIdBill()).orElseThrow();
                this.purchase_historyRepository.save(
                        Purchase_History
                                .builder()
                                .status(0)
                                .totalPrice(BigDecimal.valueOf(0))
                                .seller(dtoNew.getSeller())
                                .customer(customers)
                                .price(history.getPrice())
                                .reason(dtoNew.getReason())
                                .description(dtoNew.getDescription())
                                .typePayment(dtoNew.getTypePayment())
                                .type(dtoNew.getType())
                                .bill(bill)
                                .build()
                );
                break;
            }

            if (payDebit.compareTo(BigDecimal.valueOf(0)) > 0) {
                history.setTotalPrice(history.getPrice());
                history.setPrice(BigDecimal.valueOf(0));
                history.setStatus(1);
                history.setCustomer(customers);
                this.purchase_historyRepository.save(history);
                customers.setDebit(customers.getDebit().subtract(history.getTotalPrice()));
                this.customersRepository.save(customers);
            }


        }

        return null;
    }

    @Override
    public List<PurchaseHistoryDTO> paymentDebitCustomerForBill(Long idCustomer, List<PurchaseHistoryDTO> dtoListTarget) {
        List<PurchaseHistoryDTO> dtoListMain = this.displayDebitCustomer(idCustomer);
        for (PurchaseHistoryDTO dtoTarget : dtoListTarget) {
            Purchase_History history = this.purchase_historyRepository.findById(dtoTarget.getId()).orElseThrow();
            if (history.getPrice().compareTo(dtoTarget.getPrice()) < 0) {
                throw new BadRequestException("Too money");
            }
            BigDecimal payDebit = history.getPrice().subtract(dtoTarget.getPrice());
            if (payDebit.compareTo(BigDecimal.valueOf(0)) == 0) {
                history.setTotalPrice(dtoTarget.getPrice());
                history.setPrice(payDebit);
                history.setStatus(1);
                Customers customers = this.customersRepository.findById(idCustomer).orElseThrow();
                history.setCustomer(customers);
                this.purchase_historyRepository.save(history);
                customers.setDebit(customers.getDebit().subtract(history.getTotalPrice()));
                this.customersRepository.save(customers);
            }

            if (payDebit.compareTo(BigDecimal.valueOf(0)) > 0) {
                history.setTotalPrice(dtoTarget.getPrice());
                history.setPrice(payDebit);
                history.setStatus(1);
                Customers customers = this.customersRepository.findById(idCustomer).orElseThrow();
                history.setCustomer(customers);
                this.purchase_historyRepository.save(history);
                customers.setDebit(customers.getDebit().subtract(history.getTotalPrice()));
                Bill bill = this.billRepository.findById(history.getBill().getId()).orElseThrow();
                this.purchase_historyRepository.save(
                        Purchase_History
                                .builder()
                                .status(0)
                                .totalPrice(BigDecimal.valueOf(0))
                                .seller(history.getSeller())
                                .customer(customers)
                                .price(history.getPrice())
                                .reason(history.getReason())
                                .description(history.getDescription())
                                .typePayment(history.getTypePayment())
                                .type(history.getType())
                                .bill(bill)
                                .build()
                );
            }


        }
        return dtoListMain;
    }


}






