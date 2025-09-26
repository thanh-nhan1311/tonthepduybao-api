package com.tonthepduybao.api.security;

import com.tonthepduybao.api.security.exception.SecurityAuthenticationEntryPoint;
import com.tonthepduybao.api.security.filter.SecurityAuthenticationFilter;
import com.tonthepduybao.api.security.service.UserDetailsServiceImpl;
import com.tonthepduybao.api.service.systemLog.SystemLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

/**
 * SecurityConfiguration
 *
 * @author khal
 * @since 2022/12/24
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SystemLogService systemLogService;
    private final UserDetailsServiceImpl userDetailsService;
    private final SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        return authenticationManagerBuilder.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            var cors = new CorsConfiguration();
            cors.setAllowCredentials(true);
            cors.setAllowedHeaders(List.of("*"));
            cors.setAllowedOrigins(List.of(
                "http://localhost:7900",
                "http://157.66.81.116"
            ));
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            return cors;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(new SecurityAuthenticationFilter(systemLogService, userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/favicon.ico",
                                "/ttdb/api/mock/**",
                                "/ttdb/api/auth/login",
                                "/ttdb/api/auth/reset-password",
                                "/ttdb/api/branch/public/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(securityAuthenticationEntryPoint));

        return http.build();
    }

}
