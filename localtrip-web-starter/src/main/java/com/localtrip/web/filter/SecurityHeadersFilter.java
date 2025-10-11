package com.localtrip.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 보안 헤더 설정 필터
 */
public class SecurityHeadersFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // XSS 보호
        response.setHeader("X-XSS-Protection", "1; mode=block");
        
        // 콘텐츠 타입 스니핑 방지
        response.setHeader("X-Content-Type-Options", "nosniff");
        
        // 클릭재킹 방지 (iframe 사용 제한)
        response.setHeader("X-Frame-Options", "DENY");
        
        // HSTS (HTTPS 강제) - 프로덕션 환경에서만 활성화
        if (request.isSecure()) {
            response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        }
        
        // 리퍼러 정책 설정
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        
        // 권한 정책 설정 (불필요한 브라우저 기능 비활성화)
        response.setHeader("Permissions-Policy", 
            "geolocation=(self), microphone=(), camera=(), payment=(), usb=()");
        
        // Content Security Policy (기본 설정)
        response.setHeader("Content-Security-Policy", 
            "default-src 'self'; " +
            "script-src 'self' 'unsafe-inline'; " +
            "style-src 'self' 'unsafe-inline'; " +
            "img-src 'self' data: https:; " +
            "font-src 'self' https:; " +
            "connect-src 'self' https:; " +
            "media-src 'self'; " +
            "object-src 'none'; " +
            "frame-ancestors 'none';"
        );
        
        filterChain.doFilter(request, response);
    }
}
