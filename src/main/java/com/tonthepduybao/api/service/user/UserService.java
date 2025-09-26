package com.tonthepduybao.api.service.user;


import com.tonthepduybao.api.entity.Role;
import com.tonthepduybao.api.entity.User;
import com.tonthepduybao.api.model.user.UserCreateForm;
import com.tonthepduybao.api.model.user.UserData;
import com.tonthepduybao.api.model.user.UserUpdateForm;
import com.tonthepduybao.api.security.model.AuthRequest;
import com.tonthepduybao.api.security.model.AuthResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * UserService
 *
 * @author khal
 * @since 2022/05/08
 */
public interface UserService {

    User getMe();

    User get(Long id);

    AuthResponse login(AuthRequest request);

    List<UserData> getAll(String search, String status, String role);

    void create(UserCreateForm form);

    void update(UserUpdateForm form);

    void updateAvatar(MultipartFile avatar);

    void resetPassword(String email);

    List<Role> getAllRole();

    void delete(Long id);

}
