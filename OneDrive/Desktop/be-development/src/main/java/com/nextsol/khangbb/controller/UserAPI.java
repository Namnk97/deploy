package com.nextsol.khangbb.controller;


import com.nextsol.khangbb.model.LoginDTO;
import com.nextsol.khangbb.model.ScreenDTO;
import com.nextsol.khangbb.model.UserDTO;
import com.nextsol.khangbb.service.UserService;
import com.nextsol.khangbb.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/user")
public class UserAPI {

    @Autowired
    private UserService userService;

    @GetMapping
    private ResponseEntity<?> findAll(Pageable pageable) {

        return ResponseUtil.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> findById(@PathVariable Long id) {

        return ResponseUtil.ok(userService.findById(id));
    }

    @PostMapping
    private ResponseEntity<?> save(@RequestBody UserDTO dto) {
        return ResponseUtil.ok(userService.save(dto));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> delete(@PathVariable Long id) {
        userService.remove(id);
        return ResponseUtil.ok(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody UserDTO userDTO) {
        LoginDTO result = userService.login(userDTO);
        if (result == null)
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping
    private ResponseEntity<?> update(@RequestBody UserDTO dto) {
        return userService.updateUser(dto);
    }

    @PostMapping("/branch-vip")
    private ResponseEntity<?> updateBranchVip(@RequestBody Long idBranch) {
        return ResponseUtil.ok(userService.updateBranchVip(idBranch));
    }


    @PostMapping("/find-requement")
    private ResponseEntity<?> findRequement(@RequestBody UserDTO dto) {
        return ResponseUtil.ok(userService.findByRequement(dto));
    }

    @GetMapping("/get-user-info")
    public Object getUserInfo() {
        return userService.getInfoUser();
    }

    @PostMapping("/decentralization-screen") // Phân quyền màn hình cho user
    public ResponseEntity<?> decentralizationScreen(@RequestBody ScreenDTO screenDTO) {
        return userService.updateScreen(screenDTO);
    }
}
