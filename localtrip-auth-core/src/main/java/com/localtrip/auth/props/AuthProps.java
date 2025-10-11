package com.localtrip.auth.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "localtrip.auth")
public class AuthProps {
    
    /** 인증 기능 활성화 여부 */
    private boolean enabled = true;
    
    /** API Gateway 내부 통신 토큰 (보안) */
    private String gatewayToken;
    
    /** 인증 불필요 Public 경로 */
    private String[] publicPaths = {
        "/actuator/**",
        "/health",
        "/api/public/**",
        "/swagger-ui/**",
        "/v3/api-docs/**"
    };
    
    /** 관리자 전용 경로 */
    private String[] adminPaths = {
        "/api/admin/**",
        "/actuator/**"
    };
    
    // Getters and Setters
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getGatewayToken() {
        return gatewayToken;
    }
    
    public void setGatewayToken(String gatewayToken) {
        this.gatewayToken = gatewayToken;
    }
    
    public String[] getPublicPaths() {
        return publicPaths;
    }
    
    public void setPublicPaths(String[] publicPaths) {
        this.publicPaths = publicPaths;
    }
    
    public String[] getAdminPaths() {
        return adminPaths;
    }
    
    public void setAdminPaths(String[] adminPaths) {
        this.adminPaths = adminPaths;
    }
}