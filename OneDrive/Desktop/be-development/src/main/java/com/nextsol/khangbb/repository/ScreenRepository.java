package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.entity.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreenRepository extends JpaRepository<Screen, Long> {

	Screen findByUrl (String url);
}
