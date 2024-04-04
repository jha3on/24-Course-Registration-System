package com.demo.global.configuration.security.login;

import com.demo.domain.entity.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginDetails implements UserDetails {
    private Long id;
    private String name;
    private String number;
    private String password;

    public static UserLoginDetails of(User user) {
        return new UserLoginDetails(user.getId(), user.getName(), user.getNumber(), user.getPassword());
    }

    @Override
    public String getUsername() {
        return this.number;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}