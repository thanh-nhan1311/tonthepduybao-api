package com.tonthepduybao.api.security.model;

import com.tonthepduybao.api.entity.User;
import lombok.Builder;

/**
 * AuthenticationRequest
 *
 * @author khal
 * @since 2022/12/24
 */
@Builder
public record AuthResponse(String accessToken, String expiredTime, User user) {
}
