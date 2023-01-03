package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.entity.Bonus;
import com.nextsol.khangbb.repository.BonusRepository;
import com.nextsol.khangbb.service.BonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BonusServiceImpl implements BonusService {
    @Autowired
    private BonusRepository bonusRepository;

    @Override
    public Optional<Bonus> findActivatedBonusCode(String bonusCode) {
        Integer activated = 0;
        return bonusRepository.findByBonusCodeAndActivated(bonusCode, activated);
    }

    @Override
    public Optional<Bonus> findByBonusCode(String bonusCode) {
        return bonusRepository.findByBonusCode(bonusCode);
    }

    @Override
    public Page<Bonus> findAll(Pageable pageable) {
        return bonusRepository.findAll(pageable);
    }

    @Override
    public Optional<Bonus> findById(Long id) {
        return bonusRepository.findById(id);
    }

    @Override
    public Bonus create(Bonus bonus) {
        return bonusRepository.save(bonus);
    }
}
