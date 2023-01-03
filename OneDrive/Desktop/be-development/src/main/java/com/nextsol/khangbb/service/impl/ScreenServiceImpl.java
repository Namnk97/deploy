package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.entity.Screen;
import com.nextsol.khangbb.repository.ScreenRepository;
import com.nextsol.khangbb.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScreenServiceImpl implements ScreenService {

	@Autowired
	private ScreenRepository screenRepository;

	@Override
	public Page<Screen> findAll(Pageable pageable) {
		return null;
	}

	@Override
	public Optional<Screen> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public Screen create(Screen screen) {
		return screenRepository.save(screen);
	}

}
