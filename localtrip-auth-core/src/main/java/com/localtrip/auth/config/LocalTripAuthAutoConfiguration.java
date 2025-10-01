package com.localtrip.auth.config;

import com.localtrip.auth.props.AuthProps;
import com.localtrip.auth.handler.AuthenticationEntryPointImpl;
import com.localtrip.auth.handler.AccessDeniedHandlerImpl;
import com.localtrip.auth.filter.GatewayAuthenticationFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AutoConfiguration
@EnableConfigurationProperties(AuthProps.class)
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@ConditionalOnProperty(prefix = "localtrip.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LocalTripAuthAutoConfiguration {
    
    @Bean
    public GatewayAuthenticationFilter gatewayAuthenticationFilter(AuthProps authProps) {
        return new GatewayAuthenticationFilter(authProps.getGatewayToken());
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, 
                                          AuthProps authProps,
                                          AuthenticationEntryPointImpl authenticationEntryPoint,
                                          AccessDeniedHandlerImpl accessDeniedHandler,
                                          GatewayAuthenticationFilter gatewayAuthenticationFilter) throws Exception {
        
        System.out.println("=== Security Filter Chain 설정 시작 ===");
        System.out.println("Public Paths: " + String.join(", ", authProps.getPublicPaths()));
        System.out.println("Admin Paths: " + String.join(", ", authProps.getAdminPaths()));
        
        http
            // CSRF 완전 비활성화
            .csrf(csrf -> csrf.disable())
            
            // CORS 설정 (web-starter에서 처리)
            .cors(cors -> cors.disable())
            
            // 세션 비활성화 (무상태 API)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 기본 로그인 폼 비활성화
            .formLogin(form -> form.disable())
            
            // HTTP Basic 인증 비활성화  
            .httpBasic(basic -> basic.disable())
            
            // URL 기반 인가 설정
            .authorizeHttpRequests(authz -> {
                System.out.println("=== URL 인가 설정 ===");
                authz
                    // Public 엔드포인트 (인증 불필요)
                    .requestMatchers(authProps.getPublicPaths()).permitAll()
                    
                    // 관리자 전용 엔드포인트
                    .requestMatchers(authProps.getAdminPaths()).hasRole("ADMIN")
                    
                    // 나머지는 인증 필요
                    .anyRequest().authenticated();
            })
            
            // 예외 처리
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(authenticationEntryPoint)  // 인증 실패
                .accessDeniedHandler(accessDeniedHandler)             // 인가 실패
            )
            
            // Gateway 인증 필터 추가
            .addFilterBefore(gatewayAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("=== Security Filter Chain 설정 완료 ===");
        return http.build();
    }
}
