package com.nextsol.khangbb.service;


import com.nextsol.khangbb.entity.Bonus;

import java.util.Optional;

public interface BonusService extends BaseService<Bonus>{

    Optional<Bonus> findActivatedBonusCode(String bonusCode);

    Optional<Bonus> findByBonusCode (String bonusCode);
}
