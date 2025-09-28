package com.localtrip.member.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 토큰 생성/검증 유틸리티
 */
@Slf4j
@Component
public class JwtUtil {
    
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;
    
    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }
    
    /**
     * Access Token 생성
     */
    public String generateAccessToken(String memberId, String email, int memberType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", memberId);
        claims.put("email", email);
        claims.put("memberType", memberType);
        claims.put("tokenType", "ACCESS");
        
        return createToken(claims, memberId, jwtProperties.getAccessTokenValidity());
    }
    
    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(String memberId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", memberId);
        claims.put("tokenType", "REFRESH");
        
        return createToken(claims, memberId, jwtProperties.getRefreshTokenValidity());
    }
    
    /**
     * 토큰에서 사용자 ID 추출
     */
    public String getMemberIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("memberId", String.class);
    }
    
    /**
     * 토큰에서 이메일 추출
     */
    public String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("email", String.class);
    }
    
    /**
     * 토큰에서 회원 타입 추출
     */
    public Integer getMemberTypeFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("memberType", Integer.class);
    }
    
    /**
     * 토큰 만료 시간 조회
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }
    
    /**
     * 토큰 유효성 검증
     */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            log.debug("토큰 검증 실패: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 토큰 만료 여부 확인
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * Refresh Token 여부 확인
     */
    public boolean isRefreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        String tokenType = claims.get("tokenType", String.class);
        return "REFRESH".equals(tokenType);
    }
    
    /**
     * 토큰 생성 공통 메서드
     */
    private String createToken(Map<String, Object> claims, String subject, long validityInMilliseconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * 토큰에서 Claims 추출
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
