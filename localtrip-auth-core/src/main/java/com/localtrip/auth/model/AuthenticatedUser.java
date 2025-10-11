package com.localtrip.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * API Gateway에서 전달받은 사용자 정보를 Spring Security에서 사용할 수 있도록 변환
 */
public class AuthenticatedUser implements UserDetails {
    
    private final String userId;
    private final String username;
    private final String email;
    private final List<String> roles;
    private final boolean enabled;
    
    public AuthenticatedUser(String userId, String username, String email, List<String> roles) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles != null ? roles : List.of("USER");
        this.enabled = true;
    }
    
    public AuthenticatedUser(String userId, String username, String email, List<String> roles, boolean enabled) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles != null ? roles : List.of("USER");
        this.enabled = enabled;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public String getPassword() {
        // API Gateway 인증 방식에서는 비밀번호 불필요
        return null;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    // 추가 사용자 정보 접근자
    public String getUserId() {
        return userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public List<String> getRoles() {
        return roles;
    }
    
    public boolean hasRole(String role) {
        return roles.contains(role.toUpperCase()) || roles.contains(role.toLowerCase());
    }
    
    public boolean hasAnyRole(String... roles) {
        for (String role : roles) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "AuthenticatedUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", enabled=" + enabled +
                '}';
    }
}
