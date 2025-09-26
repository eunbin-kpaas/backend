package com.localtrip.auth.config;

import com.localtrip.auth.filter.GatewayAuthenticationFilter;
import com.localtrip.auth.handler.AuthenticationEntryPointImpl;
import com.localtrip.auth.handler.AccessDeniedHandlerImpl;
import com.localtrip.auth.props.AuthProps;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize, @PostAuthorize 활성화
@ConditionalOnProperty(prefix = "localtrip.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SecurityConfig {

    private final AuthProps authProps;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AccessDeniedHandlerImpl accessDeniedHandler;

    public SecurityConfig(AuthProps authProps,
                         AuthenticationEntryPointImpl authenticationEntryPoint,
                         AccessDeniedHandlerImpl accessDeniedHandler) {
        this.authProps = authProps;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화 (API Gateway 환경에서는 불필요)
            .csrf(AbstractHttpConfigurer::disable)
            
            // CORS 설정 (web-starter에서 처리)
            .cors(AbstractHttpConfigurer::disable)
            
            // 세션 비활성화 (무상태 API)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 기본 로그인 폼 비활성화
            .formLogin(AbstractHttpConfigurer::disable)
            
            // HTTP Basic 인증 비활성화  
            .httpBasic(AbstractHttpConfigurer::disable)
            
            // URL 기반 인가 설정
            .authorizeHttpRequests(authz -> authz
                // Public 엔드포인트 (인증 불필요)
                .requestMatchers(authProps.getPublicPaths()).permitAll()
                
                // 관리자 전용 엔드포인트
                .requestMatchers(authProps.getAdminPaths()).hasRole("ADMIN")
                
                // 나머지는 인증 필요
                .anyRequest().authenticated()
            )
            
            // 예외 처리
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(authenticationEntryPoint)  // 인증 실패
                .accessDeniedHandler(accessDeniedHandler)             // 인가 실패
            )
            
            // Gateway 인증 필터 추가
            .addFilterBefore(gatewayAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public GatewayAuthenticationFilter gatewayAuthenticationFilter() {
        return new GatewayAuthenticationFilter(authProps.getGatewayToken());
    }
}
