package com.tonthepduybao.api.security.filter;

import com.tonthepduybao.api.app.logging.Logger;
import com.tonthepduybao.api.security.service.UserDetailsServiceImpl;
import com.tonthepduybao.api.security.utils.SecurityUtils;
import com.tonthepduybao.api.service.systemLog.SystemLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * SecurityAuthenticationFilter
 *
 * @author khal
 * @since 2023/03/04
 */
@RequiredArgsConstructor
public class SecurityAuthenticationFilter extends OncePerRequestFilter {

    private final SystemLogService systemLogService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String contentType = req.getContentType();
        HttpServletRequest request = StringUtils.hasLength(contentType) && contentType.contains("application/json")
                ? new CachedBodyHttpServletRequest(req)
                : req;

        // Extract request information
        String uri = request.getRequestURI();

        String accessToken = SecurityUtils.extractToken(request);

        // Init authentication
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null, null, Collections.emptyList());
        authenticationToken.setAuthenticated(false);

        if (StringUtils.hasLength(accessToken)) {
            try {
                // Load token from database and save authentication information into security context holder
                userDetailsService.loadUserByToken(request, accessToken);
            } catch (Exception e) {
                Logger.ERROR_LOGGER.error("Unauthenticated Access Token: {}", accessToken);
            }
        }

        // Save log each request to database
        systemLogService.save(request, null);

        // Write log to file
        if (Logger.isAccessLoggerEnable(uri))
            Logger.access(request, response);

        filterChain.doFilter(request, response);
    }

    public static boolean isStaticResources(final String uri) {
        return uri.startsWith("/dist/")
                || uri.endsWith(".js")
                || uri.endsWith(".css")
                || uri.endsWith(".png")
                || uri.endsWith(".jpg")
                || uri.endsWith(".jpeg")
                || uri.endsWith(".svg")
                || uri.endsWith(".ico")
                || uri.endsWith(".woff")
                || uri.endsWith(".woff2")
                || uri.endsWith(".ttf")
                || uri.endsWith(".map")
                || uri.endsWith(".webmanifest")
                || uri.endsWith("manifest.json")
                || uri.endsWith("favicon.ico");
    }


}
