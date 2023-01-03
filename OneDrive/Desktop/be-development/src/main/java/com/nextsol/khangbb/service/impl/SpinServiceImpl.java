package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.entity.Bonus;
import com.nextsol.khangbb.entity.CustomerPresent;
import com.nextsol.khangbb.entity.Customers;
import com.nextsol.khangbb.entity.Present;
import com.nextsol.khangbb.model.CustomerDTO;
import com.nextsol.khangbb.model.HistoryPresentDTO;
import com.nextsol.khangbb.model.SpinDTO;
import com.nextsol.khangbb.model.SpinResultDTO;
import com.nextsol.khangbb.service.*;
import com.nextsol.khangbb.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpinServiceImpl implements SpinService{
    @Autowired
    private CustomerService customerService;
    @Autowired
    private BonusService bonusService;
    @Autowired
    private PresentService presentService;
    @Autowired
    private CustomerPresentService customerPresentService;

    @Override
    public Customers findCustomersByPhone(String phone) {
        return customerService.findByPhone(phone) ;
    }

    @Override
    public SpinResultDTO spin(SpinDTO dto) {
        // check tạo mới customer
        Customers customers = findCustomersByPhone(dto.getPhone());

        if (customers == null)
            customers = customerService.save(new CustomerDTO(dto.getFullname(), dto.getPhone(), dto.getEmail(), dto.getAddress()));

        // kiểm tra mã bonus code
        List<Present> presentList = presentService.findAll();
        Optional<Bonus> optional = bonusService.findActivatedBonusCode(dto.getBonusCode());

        if (optional.isEmpty())
            return new SpinResultDTO();

        // Tạo quà cho người dùng, viết vào lịch sử quay, loại 1 mã bonus code
        dto.setCustomerId(customers.getId());

        Bonus bonus = optional.get();
        CustomerPresent customerPresent = MapperUtil.map(dto , CustomerPresent.class);
        SpinResultDTO resultDTO = new SpinResultDTO();

        bonus.setActivated(1);
        bonusService.create(bonus);
        customerPresent.setBonus(bonus);
        customerPresent.setPresent(presentList.get((int) (Math.random() * presentList.size())));
        customerPresent =  customerPresentService.create(customerPresent);

        // Trả về quà nhận được và lịch sử quà
        List<CustomerPresent> historyPresent = customerPresentService.findByCustomerId(customers.getId());
        List<HistoryPresentDTO> listDTO = new ArrayList<>();

        resultDTO.setPresent(customerPresent.getPresent());
        historyPresent.forEach(x -> {
            listDTO.add(new HistoryPresentDTO(x.getPresent(), x.getCreatedDate()));
        });
        resultDTO.setHistoryPresents(listDTO);

        return resultDTO;
    }
}
