package com.tonthepduybao.api.security.service;

import com.tonthepduybao.api.entity.AuthSession;
import com.tonthepduybao.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

/**
 * UserDetailsImpl
 *
 * @author khale
 * @since 2021/10/22
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private User user;
    private AuthSession authSession;

    public static UserDetailsImpl build(final AuthSession authSession) {
        User user = authSession.getUser();

//        SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority(user.getRole().getAuthority());
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(userAuthority);

        return new UserDetailsImpl(user.getId(), user.getUsername(), "", null, user, authSession);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !authSession.isExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Objects.nonNull(user);
    }
}
