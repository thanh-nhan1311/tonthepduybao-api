package com.tonthepduybao.api.security.service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * UserDetailsService
 *
 * @author khal
 * @since 2023/08/27
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

    void loadUserByToken(HttpServletRequest request, final String token);

}
