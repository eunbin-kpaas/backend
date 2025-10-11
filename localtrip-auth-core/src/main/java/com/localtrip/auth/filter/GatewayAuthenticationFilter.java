package com.localtrip.auth.filter;

import com.localtrip.auth.model.AuthenticatedUser;
import com.localtrip.common.exception.GlobalErrorCode;
import com.localtrip.common.exception.custom.BusinessLogicException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * API Gateway에서 전달받은 사용자 정보 헤더를 파싱하여 Spring Security Context에 설정
 */
public class GatewayAuthenticationFilter extends OncePerRequestFilter {
    
    private static final Logger log = LoggerFactory.getLogger(GatewayAuthenticationFilter.class);
    
    // API Gateway에서 전달하는 헤더명들
    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USERNAME = "X-Username";  
    private static final String HEADER_EMAIL = "X-User-Email";
    private static final String HEADER_ROLES = "X-User-Roles";
    private static final String HEADER_ENABLED = "X-User-Enabled";
    
    // Gateway 내부 통신임을 확인하는 헤더 (보안)
    private static final String HEADER_GATEWAY_TOKEN = "X-Gateway-Token";
    
    private final String expectedGatewayToken;
    private final boolean requireGatewayToken;
    
    public GatewayAuthenticationFilter(String expectedGatewayToken) {
        this.expectedGatewayToken = expectedGatewayToken;
        this.requireGatewayToken = StringUtils.hasText(expectedGatewayToken);
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Gateway 토큰 검증 (설정된 경우)
            if (requireGatewayToken && !isValidGatewayRequest(request)) {
                log.warn("Invalid gateway token from IP: {}", getClientIp(request));
                throw new BusinessLogicException(GlobalErrorCode.AUTH_TOKEN_INVALID);
            }
            
            // 사용자 정보 헤더 추출
            String userId = request.getHeader(HEADER_USER_ID);
            
            if (StringUtils.hasText(userId)) {
                // 인증된 사용자 정보가 있는 경우
                AuthenticatedUser user = extractUserFromHeaders(request);
                
                // Spring Security Context에 인증 정보 설정
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("Authenticated user: {} with roles: {}", user.getUsername(), user.getRoles());
            } else {
                // 인증되지 않은 요청 (public 엔드포인트)
                SecurityContextHolder.clearContext();
                log.debug("No user authentication found in headers");
            }
            
        } catch (Exception e) {
            log.error("Authentication filter error: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            
            if (e instanceof BusinessLogicException) {
                throw e;
            }
            
            throw new BusinessLogicException(GlobalErrorCode.AUTH_INTERNAL);
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Gateway 요청 유효성 검증
     */
    private boolean isValidGatewayRequest(HttpServletRequest request) {
        String gatewayToken = request.getHeader(HEADER_GATEWAY_TOKEN);
        return expectedGatewayToken.equals(gatewayToken);
    }
    
    /**
     * 헤더에서 사용자 정보 추출
     */
    private AuthenticatedUser extractUserFromHeaders(HttpServletRequest request) {
        String userId = request.getHeader(HEADER_USER_ID);
        String username = request.getHeader(HEADER_USERNAME);
        String email = request.getHeader(HEADER_EMAIL);
        String rolesHeader = request.getHeader(HEADER_ROLES);
        String enabledHeader = request.getHeader(HEADER_ENABLED);
        
        // 필수 필드 검증
        if (!StringUtils.hasText(userId)) {
            throw new BusinessLogicException(GlobalErrorCode.AUTH_USER_NOT_FOUND);
        }
        
        // 기본값 설정
        if (!StringUtils.hasText(username)) {
            username = userId; // userId를 username으로 사용
        }
        
        // 역할 파싱 (콤마로 구분된 문자열)
        List<String> roles;
        if (StringUtils.hasText(rolesHeader)) {
            roles = Arrays.stream(rolesHeader.split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .toList();
        } else {
            roles = List.of("USER"); // 기본 역할
        }       // 활성화 상태 파싱
        boolean enabled = !StringUtils.hasText(enabledHeader) || Boolean.parseBoolean(enabledHeader);

        return new AuthenticatedUser(userId, username, email, roles, enabled);
    }

    /**     * 클라이언트 IP 주소 추출     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}