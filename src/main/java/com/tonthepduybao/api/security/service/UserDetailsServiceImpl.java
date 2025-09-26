package com.tonthepduybao.api.security.service;

import com.tonthepduybao.api.entity.AuthSession;
import com.tonthepduybao.api.repository.AuthSessionRepository;
import com.tonthepduybao.api.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

/**
 * UserDetailsServiceImpl
 *
 * @author khale
 * @since 2021/10/22
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthSessionRepository authSessionRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        AuthSession authSession = authSessionRepository.findByUser_Username(username)
                .orElseThrow(() -> new BadCredentialsException("Unauthenticated"));
        return UserDetailsImpl.build(authSession);
    }

    @Override
    public void loadUserByToken(final HttpServletRequest request, final String accessToken) {
        AuthSession authSession = authSessionRepository.findByToken(accessToken)
                .orElseThrow(() -> new BadCredentialsException("Unauthenticated"));
        UserDetailsImpl userDetails = UserDetailsImpl.build(authSession);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
