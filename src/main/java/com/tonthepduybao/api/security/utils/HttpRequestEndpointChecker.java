package com.tonthepduybao.api.security.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Objects;

/**
 * HttpRequestEndpointChecker
 *
 * @author jackerbit77
 * @since 2024/01/04
 */
@AllArgsConstructor
@Component
public class HttpRequestEndpointChecker {

    private DispatcherServlet servlet;

    public boolean isEndpointExist(HttpServletRequest request) {
        assert servlet.getHandlerMappings() != null;
        for (HandlerMapping handlerMapping : servlet.getHandlerMappings()) {
            try {
                HandlerExecutionChain foundHandler = handlerMapping.getHandler(request);
                if (Objects.nonNull(foundHandler)) return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

}
