package com.nextsol.khangbb.service.impl;

import com.nextsol.khangbb.constant.Constant;
import com.nextsol.khangbb.entity.Branch;
import com.nextsol.khangbb.entity.Roles;
import com.nextsol.khangbb.entity.Screen;
import com.nextsol.khangbb.entity.Users;
import com.nextsol.khangbb.model.LoginDTO;
import com.nextsol.khangbb.model.ScreenDTO;
import com.nextsol.khangbb.model.UserDTO;
import com.nextsol.khangbb.repository.BranchRepository;
import com.nextsol.khangbb.repository.RolesRepository;
import com.nextsol.khangbb.repository.ScreenRepository;
import com.nextsol.khangbb.repository.UserRepository;
import com.nextsol.khangbb.security.CustomUserDetails;
import com.nextsol.khangbb.security.JwtTokenProvider;
import com.nextsol.khangbb.service.UserService;
import com.nextsol.khangbb.util.CommonUtil;
import com.nextsol.khangbb.util.DataUtil;
import com.nextsol.khangbb.util.MapperUtil;
import com.nextsol.khangbb.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.nextsol.khangbb.constant.Constant.KEY_SCREEN;
import static com.nextsol.khangbb.constant.Constant.PARENT_KEY_SCREEN;


@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImp implements UserService {


    private final UserRepository userRepository;


    private final BranchRepository branchRepository;

    private final RolesRepository rolesRepository;


    private final JwtTokenProvider jwtTokenProvider;


    private final PasswordEncoder passwordEncoder;


    private final ScreenRepository screenRepository;


    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        CustomUserDetails customUserDetails = (CustomUserDetails) this.getInfoUser();
        Branch branch = this.branchRepository.findById(customUserDetails.getUsers().getBranch().getId()).orElseThrow();
        Page<Users> usersPage = this.userRepository.findByDeletedAndBranch(0, branch, pageable);
        Page<UserDTO> userDTOS = MapperUtil.mapEntityPageIntoDtoPage(usersPage, UserDTO.class);
        return userDTOS;
    }

    @Override
    public Optional<UserDTO> findById(Long id) {
        Optional<Users> users = this.userRepository.findById(id);
        if (users.isPresent()) {
            return Optional.of(toUserDTO(users.get()));
        }

        return Optional.empty();
    }


    @Override
    public UserDTO create(UserDTO userDTO) {
        return null;
    }

    @Override
    public ResponseEntity<?> save(UserDTO userDTO) {
        UserDetails userDetails = (UserDetails) getInfoUser();
        if (userDTO.getIdRole().equals(Constant.SUPER_ADMIN_ID))
            return ResponseUtil.badRequest("Không đủ quyền hạn");
        Users userCurrent = userRepository.findByUsernameAndDeleted(userDetails.getUsername(), 0).orElse(null);
        Branch branch = new Branch();
        if (userDTO.getBranch() == null)
            branch = this.branchRepository.findById(userDTO.getIdBranch()).orElse(null);
        Roles roles = this.rolesRepository.findById(userDTO.getIdRole()).orElseThrow();
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (userDTO.getUsername() == null)
            return ResponseUtil.badRequest("Tài khoản không được để rống");
        if (userRepository.findByUsernameAndDeleted(userDTO.getUsername(), 0).isPresent())
            return ResponseUtil.badRequest("Tài khoản đã tồn tại");
        if (userDTO.getPassword() != null) {
            if (userDTO.getPassword().length() < 6)
                return ResponseUtil.badRequest("Mật khẩu không được ít hơn 6 ký tự");
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            return ResponseUtil.badRequest("Mật khẩu không được để rống");
        }
        if (userDTO.getFullname() == null)
            return ResponseUtil.badRequest("Tên người dùng không được để trống");
        if (userDTO.getFullname().matches("(.*)[^\\p{L}\\s_](.*)"))
            return ResponseUtil.badRequest("Tên người dùng không được nhập số và ký tự đặc biệt");
        if (userDTO.getFullname().length() < 6)
            return ResponseUtil.badRequest("Tên người dùng không được ít hơn 6 ký tự");
        Users users = MapperUtil.map(userDTO, Users.class);
        if (userCurrent != null && userCurrent.getRole() != null) {
            if (userCurrent.getRole().getName().equals(Constant.SUPER_ADMIN))
                users.setBranch(branch);
            else
                users.setBranch(userCurrent.getBranch());
        }
        users.setRole(roles);
        users.setDeleted(0);
        this.userRepository.save(users);
        return ResponseUtil.ok(userDTO);
    }

    @Override
    public ResponseEntity<?> remove(Long id) {
        Users users = userRepository.findById(id).orElse(null);
        if (users != null) {
            UserDetails userDetails = (UserDetails) getInfoUser();
            Users currentUser = userRepository.findByUsernameAndDeleted(userDetails.getUsername(),0).orElseThrow();
            if (currentUser.getId().equals(id))
                return ResponseUtil.badRequest("Bạn không thể tự hủy");
            if(users.getRole().getId().equals(Constant.USER_ID))
                return ResponseUtil.badRequest("Không đủ quyền hạn");
            users.setDeleted(1);
            this.userRepository.save(users);
            return ResponseUtil.badRequest("Xóa thành công");
        }
        return ResponseUtil.badRequest("Xóa thất bại");
    }

    @Override
    public UserDTO toUserDTO(Users users) {
        if (users != null) {
            return MapperUtil.map(users, UserDTO.class);
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByUsernameAndDeleted(username, 0);
        return user.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public UserDetails loadUserById(Long id) {
        Optional<Users> user = this.userRepository.findById(id);
        return user.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(id.toString()));
    }

    @Override
    public LoginDTO login(UserDTO userDTO) {
        Optional<Users> user = userRepository.findByUsernameAndDeleted(userDTO.getUsername(), 0);

        if (user.isPresent()) {
            boolean checkPassword = passwordEncoder.matches(userDTO.getPassword(), user.get().getPassword());
            if (checkPassword) {

                return generateToken(user);
            }
        }
        return null;
    }

    @Override
    public Object getInfoUser() {

        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public ResponseEntity<?> updateUser(UserDTO userDTO) {
        Optional<Users> optionalUsers = userRepository.findById(userDTO.getId());

        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            UserDetails userDetails = (UserDetails) getInfoUser();
            Users currentUser = userRepository.findByUsernameAndDeleted(userDetails.getUsername(),0).orElseThrow();
            if (users.getRole().getId().equals(Constant.SUPER_ADMIN_ID) && !currentUser.getRole().getId().equals(Constant.SUPER_ADMIN_ID))
                return ResponseUtil.badRequest("Không đủ quyền hạn");
            if (userDTO.getPassword() != null) {
                if (userDTO.getPassword().length() < 6)
                    return ResponseUtil.badRequest("Mật khẩu không được ít hơn 6 ký tự");
                userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            } else {
                userDTO.setPassword(optionalUsers.get().getPassword());
            }
            if (userDTO.getFullname() == null)
                return ResponseUtil.badRequest("Tên người dùng không được để trống");
            if (userDTO.getFullname().matches("(.*)[^\\p{L}\\s_](.*)"))
                return ResponseUtil.badRequest("Tên người dùng không được nhập số và ký tự đặc biệt");
            if (userDTO.getFullname().length() < 6)
                return ResponseUtil.badRequest("Tên người dùng không được ít hơn 6 ký tự");

            if (userDTO.getUsername() != null)
                return ResponseUtil.badRequest(Constant.UPDATE_FAIL);

            if (userDTO.getIdRole() != null)
                rolesRepository.findById(userDTO.getIdRole()).ifPresent(users::setRole);
            if (userDTO.getIdBranch() != null)
                branchRepository.findById(userDTO.getIdBranch()).ifPresent(users::setBranch);
            users.setDeleted(0);
            userRepository.save(users);
            return ResponseUtil.ok(MapperUtil.map(users, UserDTO.class));
        }
        return ResponseUtil.badRequest(Constant.UPDATE_FAIL);
    }

    @Override
    public ResponseEntity<?> updateScreen(ScreenDTO screenDTO) {
        Users userTarget = userRepository.findByIdAndDeleted(screenDTO.getUserId(), 0);
        if (userTarget == null) {
            return ResponseUtil.notFound(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng này");
        }
        List<String> keysParent = new LinkedList<>();
        List<String> keysChild = new LinkedList<>();
        List<String> valuesKeyScreen = CommonUtil.flattenListOfListsStream(KEY_SCREEN.values());
        screenDTO.getUrls().forEach(key -> {
            if (PARENT_KEY_SCREEN.contains(key)) {
                keysParent.add(key);
            }
            if (!key.equals("root") && !key.equals("sell-product") && valuesKeyScreen.contains(key)) {
                keysChild.add(key);
            }
        });
        if (keysParent.size() == 0 && keysChild.size() == 0)
            return ResponseUtil.notFound(HttpStatus.NOT_FOUND, "Danh sách màn hình không hợp lệ");
        List<Screen> screenList = new LinkedList<>();
        if (keysParent.size() > 0) {
            for (String key : keysParent) {
                List<String> listKeyScreen = KEY_SCREEN.get(key);
                Screen screen = screenRepository.findByUrl(key);
                if (screen == null) {
                    Screen newScreen = new Screen();
                    newScreen.setUrl(key);
                    newScreen = screenRepository.save(newScreen);
                    screenList.add(newScreen);
                } else {
                    screenList.add(screen);
                }
                if (listKeyScreen.size() == 0) { // Allow all screen and return
                    userTarget.setScreens(screenList);
                    userRepository.save(userTarget);
                    return ResponseUtil.ok(HttpStatus.OK);
                }
            }
        }
        for (String key : keysChild) {
            Screen screen = screenRepository.findByUrl(key); // Dành cho trường hợp dữ liệu không đầy đủ
            if (screen == null) {
                Screen newScreen = new Screen();
                newScreen.setUrl(key);
                newScreen = screenRepository.save(newScreen);
                screenList.add(newScreen);
            } else {
                screenList.add(screen);
            }
        }
        userTarget.setScreens(screenList);
        userRepository.save(userTarget);
        return ResponseUtil.ok(HttpStatus.OK);
    }

    @Override
    public UserDTO updateBranchVip(Long idBranch) {
        Users users = getCurrentUser();
        if (users.getRole().getName().equalsIgnoreCase("VIP")) {
            Branch branch = this.branchRepository.findById(idBranch).orElseThrow();
            users.setBranch(branch);
            this.userRepository.save(users);
        }
        return null;
    }

    @Override
    public List<UserDTO> findByRequement(UserDTO dto) {
        return this.userRepository.findByRequement(dto);
    }

    @Override
    public Users getCurrentUser() {
        CustomUserDetails customUserDetails = (CustomUserDetails) this.getInfoUser();
        String currentUser = customUserDetails.getUsers().getUsername();
        return userRepository.findByUsernameAndDeleted(currentUser, 0).orElseThrow();
    }


    public LoginDTO generateToken(Optional<Users> user) {
        String token = jwtTokenProvider.generateToken(new CustomUserDetails(user.get()));
        return LoginDTO.builder()
                .id(user.get().getId())
                .fullname(user.get().getFullname())
                .username(user.get().getUsername())
                .nameRole(user.get().getRole().getName())
                .token(token)
                .build();
    }


}
