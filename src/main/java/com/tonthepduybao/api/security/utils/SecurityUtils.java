package com.tonthepduybao.api.security.utils;

import com.tonthepduybao.api.entity.User;
import com.tonthepduybao.api.security.service.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * SecurityUtils
 *
 * @author khal
 * @since 2023/08/27
 */
public class SecurityUtils {

    public static String extractToken(final HttpServletRequest request)  {
        String authorizationHeader = request.getHeader("Authorization");
        return StringUtils.hasLength(authorizationHeader)
                ? authorizationHeader.substring(6).trim()
                : "";
    }

    public static UserDetailsImpl getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = Objects.nonNull(authentication) ? authentication.getPrincipal() : null;

        return Objects.nonNull(authentication) && Objects.nonNull(principal) && !principal.equals("anonymousUser")
                ? (UserDetailsImpl) principal
                : null;
    }

    public static Long getCurrentUserId(boolean throwable) {
        User user = getCurrentUser(throwable);
        return Objects.nonNull(user) ? user.getId() : null;
    }

    public static User getCurrentUser() {
        return getCurrentUser(true);
    }

    public static User getCurrentUser(final boolean throwable) {
        UserDetailsImpl userDetails = SecurityUtils.getUserDetails();
        if (Objects.isNull(userDetails)) {
            if (throwable) throw new BadCredentialsException("Unauthenticated");
            else return null;
        }

        return userDetails.getUser();
    }

}
