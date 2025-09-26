package com.localtrip.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.localtrip.common.exception.ErrorResponse;
import com.localtrip.common.exception.GlobalErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인증 실패 시 처리 (401 Unauthorized)
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    
    private static final Logger log = LoggerFactory.getLogger(AuthenticationEntryPointImpl.class);
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        GlobalErrorCode errorCode = GlobalErrorCode.AUTH_TOKEN_INVALID;
        
        log.warn("Authentication failed for {} {} - {}", 
                request.getMethod(), request.getRequestURI(), authException.getMessage());
        
        // 에러 응답 생성
        ErrorResponse errorResponse = ErrorResponse.from(errorCode, request.getRequestURI());
        
        // JSON 응답 설정
        response.setStatus(errorCode.getStatus());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
