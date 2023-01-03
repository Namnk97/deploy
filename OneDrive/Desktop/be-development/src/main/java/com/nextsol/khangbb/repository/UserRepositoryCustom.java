package com.nextsol.khangbb.repository;

import com.nextsol.khangbb.model.UserDTO;

import java.util.List;

public interface UserRepositoryCustom {

    List<UserDTO> findByRequement(UserDTO dto);
}
