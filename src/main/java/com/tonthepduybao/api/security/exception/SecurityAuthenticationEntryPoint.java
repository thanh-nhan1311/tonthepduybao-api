package com.tonthepduybao.api.security.exception;

import com.tonthepduybao.api.app.logging.Logger;
import com.tonthepduybao.api.security.utils.HttpRequestEndpointChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * JwtAuthenticationEntryPoint
 *
 * @author khale
 * @since 2021/10/23
 */
@RequiredArgsConstructor
@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HttpRequestEndpointChecker httpRequestEndpointChecker;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (httpRequestEndpointChecker.isEndpointExist(request)) {
            Logger.ERROR_LOGGER.error("Unauthorized error: {}", authException.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
        }
    }

}
