package com.tonthepduybao.api.service.user;

import com.tonthepduybao.api.app.constant.Constant;
import com.tonthepduybao.api.app.exception.model.ExistenceException;
import com.tonthepduybao.api.app.exception.model.SystemException;
import com.tonthepduybao.api.app.helper.MailHelper;
import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.app.helper.S3Helper;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.entity.*;
import com.tonthepduybao.api.entity.enumeration.ERole;
import com.tonthepduybao.api.entity.enumeration.EUserStatus;
import com.tonthepduybao.api.model.user.UserCreateForm;
import com.tonthepduybao.api.model.user.UserData;
import com.tonthepduybao.api.model.user.UserUpdateForm;
import com.tonthepduybao.api.repository.*;
import com.tonthepduybao.api.security.model.AuthRequest;
import com.tonthepduybao.api.security.model.AuthResponse;
import com.tonthepduybao.api.security.utils.SecurityUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * UserServiceImpl
 *
 * @author khal
 * @since 2022/05/08
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final MessageHelper messageHelper;
    private final MailHelper mailHelper;
    private final S3Helper s3Helper;

    private final Constant constant;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final HashRepository hashRepository;
    private final AuthSessionRepository authSessionRepository;

    @Override
    public User getMe() {
        return SecurityUtils.getCurrentUser();
    }

    @Override
    public User get(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(messageHelper.buildUnauthorized());
    }

    @Override
    public AuthResponse login(final AuthRequest request) {
        Long branchId = request.branchId();
        String username = request.username();
        String password = request.password();

        Branch branch = getBranch(branchId);
        String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = userRepository.findByUsernameAndPasswordAndBranchAndDeleted(username, hashPassword, branch, false)
                .orElseThrow(messageHelper.buildUnauthorized());

        String hash = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + username + password;
        String accessToken = DigestUtils.md5DigestAsHex(hash.getBytes());
        String expiredTime = LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern(TimeUtils.DTF_dd_MM_yyyy_HH_mm_ss));

        AuthSession authSession = AuthSession.builder()
                .token(accessToken)
                .expired(false)
                .loginTime(LocalDateTime.now())
                .user(user)
                .build();
        authSessionRepository.saveAndFlush(authSession);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .expiredTime(expiredTime)
                .user(user)
                .build();
    }

    @Override
    public List<UserData> getAll(final String search, final String status, final String role) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);
        String searchParam = "%" + search.toLowerCase() + "%";

        List<EUserStatus> statusList = StringUtils.hasLength(status)
                ? List.of(EUserStatus.valueOf(status))
                : List.of(EUserStatus.ACTIVE, EUserStatus.BLOCKED);

        List<ERole> eRoles = StringUtils.hasLength(role)
                ? List.of(ERole.valueOf(role))
                : List.of(ERole.values());
        List<Role> roleList = eRoles
                .stream()
                .map(eRole -> roleRepository
                        .findById(eRole)
                        .orElseThrow(messageHelper.buildDataNotFound("Role với ID =", eRole.name())))
                .toList();

        return userRepository.searchAll(searchParam, statusList, roleList, currentUserId)
                .stream()
                .map(item -> {
                    UserData userData = DataBuilder.to(item, UserData.class);

                    String createdBy = Objects.nonNull(item.getCreatedBy()) ? userRepository.getFullNameById(item.getCreatedBy()) : "";
                    String updatedBy = Objects.nonNull(item.getUpdatedBy()) ? userRepository.getFullNameById(item.getUpdatedBy()) : "";

                    userData.setCreatedBy(createdBy);
                    userData.setUpdatedBy(updatedBy);

                    return userData;
                }).toList();
    }

    @Override
    public void create(final UserCreateForm form) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);
        String now = TimeUtils.nowStr();
        String username = form.username();

        boolean isExistUsername = userRepository.existsByUsername(username);
        if (isExistUsername)
            throw new ExistenceException(List.of("Tên đăng nhập ", username));

        Branch branch = getBranch(form.branchId());
        ERole eRole = ERole.valueOf(form.roleId());
        Role role = roleRepository.findById(eRole)
                .orElseThrow(messageHelper.buildDataNotFound("Role với ID =", eRole.name()));
        String hashPassword = DigestUtils.md5DigestAsHex(form.password().getBytes());

        User user = User.builder()
                .username(username)
                .password(hashPassword)
                .fullName(username) // Default using username as full name
                .status(EUserStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .createdBy(currentUserId)
                .updatedBy(currentUserId)
                .role(role)
                .branch(branch)
                .deleted(false)
                .build();
        userRepository.saveAndFlush(user);
    }

    @Override
    public void update(final UserUpdateForm form) {
        User user = getMe();

        user.setFullName(form.fullName());
        user.setPhone(form.phone());
        user.setEmail(form.email());
        user.setAddress(form.address());
        user.setUpdatedAt(TimeUtils.nowStr());
        user.setUpdatedBy(user.getId());
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateAvatar(final MultipartFile avatar) {
        User user = getMe();

        String endpoint = s3Helper.upload("users/" + user.getId(), avatar, "");
        user.setAvatar(endpoint);
        user.setUpdatedAt(TimeUtils.nowStr());
        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void resetPassword(final String email) {
        User user = userRepository.findByEmailAndDeleted(email, false)
                .orElseThrow(messageHelper.buildDataNotFound("Địa chỉ email =", email));

        Faker faker = new Faker();
        String newPassword = faker.internet().password(16, 20, true, true, true);
        String newPasswordEncoder = DigestUtils.md5DigestAsHex(newPassword.getBytes());

        user.setPassword(newPasswordEncoder);
        user.setCreatedAt(TimeUtils.nowStr());
        userRepository.saveAndFlush(user);

        saveHash(user.getId(), newPassword, newPasswordEncoder);

        Map<String, Object> properties = new HashMap<>();
        properties.put("fullName", user.getFullName());
        properties.put("newPassword", newPassword);
        properties.put("mailSupport", constant.getMailSender());

        try {
            mailHelper.send(constant.getMailSender(), email,
                    "[NEXIMPERIO] Request Reset Password",
                    properties,
                    "mail/forgot-password.html");
        } catch (MessagingException e) {
            throw new SystemException();
        }
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public void delete(final Long id) {
        String now = TimeUtils.nowStr();

        User user = get(id);
        user.setUsername(user.getUsername() + "__deleted__" + now);
        user.setDeleted(true);
        user.setUpdatedAt(now);
        user.setUpdatedBy(SecurityUtils.getCurrentUserId(false));

        userRepository.saveAndFlush(user);
    }

    private void saveHash(final Long userId, final String pwd, final String hashPwd) {
        hashRepository.deleteByUserId(userId);
        Hash hash = Hash.builder()
                .pwd(pwd)
                .hash(hashPwd)
                .userId(userId)
                .build();
        hashRepository.saveAndFlush(hash);
    }

    private Branch getBranch(final Long branchId) {
        return Objects.isNull(branchId)
                ? null
                : branchRepository.findById(branchId)
                .orElseThrow(messageHelper.buildDataNotFound("Chi nhánh với ID =", branchId));
    }

}
