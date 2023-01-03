package com.nextsol.khangbb.service;

import com.nextsol.khangbb.entity.Users;
import com.nextsol.khangbb.model.LoginDTO;
import com.nextsol.khangbb.model.ScreenDTO;
import com.nextsol.khangbb.model.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService, BaseService<UserDTO> {

    ResponseEntity<?> save(UserDTO userDTO);

    ResponseEntity<?> remove(Long id);

    UserDTO toUserDTO(Users users);

    UserDetails loadUserById(Long id);

    LoginDTO login(UserDTO userDTO);

    Object getInfoUser();

    ResponseEntity<?> updateUser(UserDTO userDTO);

    ResponseEntity<?> updateScreen(ScreenDTO screenDTO);

    UserDTO updateBranchVip(Long idBranch);

    List<UserDTO> findByRequement(UserDTO dto);

    Users getCurrentUser();
}
