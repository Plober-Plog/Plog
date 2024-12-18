package com.plog.backend.global.auth;

import com.plog.backend.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Setter
@Getter
public class PloberUserDetails implements UserDetails {
    User user;
    boolean accountNonExpired = true; // 기본값을 true로 설정
    boolean accountNonLocked = true; // 기본값을 true로 설정
    boolean credentialNonExpired = true; // 기본값을 true로 설정
    boolean enabled = true; // 기본값을 true로 설정
    List<GrantedAuthority> roles = new ArrayList<>();

    public PloberUserDetails(User user) {
        super();
        this.user = user;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getSearchId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    public void setAuthorities(List<GrantedAuthority> roles) {
        this.roles = roles;
    }
}
