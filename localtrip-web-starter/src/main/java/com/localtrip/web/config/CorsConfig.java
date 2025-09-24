package com.localtrip.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS 설정
 */
@Configuration
@ConditionalOnProperty(prefix = "localtrip.web.cors", name = "enabled", havingValue = "true", matchIfMissing = true)
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 허용할 Origin 설정 (환경변수나 프로퍼티로 관리 권장)
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",           // React 개발서버
            "http://localhost:3001",           // Next.js 개발서버  
            "https://*.localtrip.com",         // 프로덕션 도메인
            "https://*.vercel.app"             // Vercel 배포
        ));
        
        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        
        // 허용할 헤더
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type", 
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "X-Request-ID"
        ));
        
        // 응답에 노출할 헤더
        configuration.setExposedHeaders(Arrays.asList(
            "X-Request-ID",
            "X-Total-Count",
            "Authorization"
        ));
        
        // 자격 증명 허용 (쿠키, Authorization 헤더 등)
        configuration.setAllowCredentials(true);
        
        // 브라우저 캐시 시간 (초)
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
