package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.entity.Role;
import com.tonthepduybao.api.entity.User;
import com.tonthepduybao.api.model.user.UserCreateForm;
import com.tonthepduybao.api.model.user.UserData;
import com.tonthepduybao.api.model.user.UserUpdateForm;
import com.tonthepduybao.api.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * UserController
 *
 * @author khal
 * @since 2023/07/14
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getMe() {
        return userService.getMe();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@PathVariable(name = "id") @NotNull Long id) {
        return userService.getMe();
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserData> getAll(@RequestParam(name = "search", defaultValue = "") String search,
                                 @RequestParam(name = "status", defaultValue = "") String status,
                                 @RequestParam(name = "role", defaultValue = "") String role) {
        return userService.getAll(search, status, role);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody @NotNull UserCreateForm form) {
        userService.create(form);
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public void update(@Valid @RequestBody @NotNull UserUpdateForm form) {
        userService.update(form);
    }

    @PostMapping(value = "/update/avatar", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateAvatar(@Valid @NotNull MultipartFile avatar) {
        userService.updateAvatar(avatar);
    }

    @GetMapping(value = "/roles", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Role> getAllRole() {
        return userService.getAllRole();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable(name = "id") Long id) {
        userService.delete(id);
    }

}
