package com.localtrip.auth.util;

import com.localtrip.auth.model.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 현재 인증된 사용자 정보 조회 유틸리티
 */
public class SecurityUtil {

    private SecurityUtil() {
        // 유틸리티 클래스 - 인스턴스 생성 방지
    }

    /**
     * 현재 인증된 사용자 정보 반환
     */
    public static AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthenticatedUser) {
            return (AuthenticatedUser) principal;
        }
        
        return null;
    }

    /**
     * 현재 사용자 ID 반환
     */
    public static String getCurrentUserId() {
        AuthenticatedUser user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }

    /**
     * 현재 사용자명 반환
     */
    public static String getCurrentUsername() {
        AuthenticatedUser user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }

    /**
     * 현재 사용자 이메일 반환
     */
    public static String getCurrentUserEmail() {
        AuthenticatedUser user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    /**
     * 현재 사용자가 특정 역할을 가지고 있는지 확인
     */
    public static boolean hasRole(String role) {
        AuthenticatedUser user = getCurrentUser();
        return user != null && user.hasRole(role);
    }

    /**
     * 현재 사용자가 여러 역할 중 하나라도 가지고 있는지 확인
     */
    public static boolean hasAnyRole(String... roles) {
        AuthenticatedUser user = getCurrentUser();
        return user != null && user.hasAnyRole(roles);
    }

    /**
     * 현재 사용자가 관리자인지 확인
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * 현재 사용자가 인증되었는지 확인
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() && 
               getCurrentUser() != null;
    }

    /**
     * 현재 사용자가 특정 리소스의 소유자인지 확인
     */
    public static boolean isResourceOwner(String resourceOwnerId) {
        String currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(resourceOwnerId);
    }

    /**
     * 현재 사용자가 리소스에 접근 권한이 있는지 확인 (소유자 또는 관리자)
     */
    public static boolean canAccessResource(String resourceOwnerId) {
        return isAdmin() || isResourceOwner(resourceOwnerId);
    }
}
