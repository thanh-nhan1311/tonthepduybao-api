package com.tonthepduybao.api.security.controller;

import com.tonthepduybao.api.app.constant.AppConstant;
import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.security.model.AuthRequest;
import com.tonthepduybao.api.security.model.AuthResponse;
import com.tonthepduybao.api.service.user.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController
 *
 * @author khale
 * @since 2021/10/22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/auth")
public class AuthController {

    private final MessageHelper messageHelper;
    private final UserService userService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = userService.login(authRequest);
        return ResponseEntity.ok(authResponse);
    }


    @GetMapping(value = "/reset-password")
    private void resetPassword(@RequestParam(value = "email") @NotBlank @Length(max = 320) String email) {
        userService.resetPassword(email);
    }

    @GetMapping(value = "/logout")
    private void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(AppConstant.ACCESS_TOKEN_COOKIE, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
