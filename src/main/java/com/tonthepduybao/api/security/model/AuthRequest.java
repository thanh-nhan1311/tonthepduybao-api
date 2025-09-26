package com.tonthepduybao.api.security.model;

/**
 * AuthenticationRequest
 *
 * @author khal
 * @since 2022/12/24
 */
public record AuthRequest(String username, String password, Long branchId) {
}
