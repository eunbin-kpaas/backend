package com.localtrip.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.localtrip.common.exception.ErrorResponse;
import com.localtrip.common.exception.GlobalErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 인가 실패 시 처리 (403 Forbidden)
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    
    private static final Logger log = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                      AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        // 403 Forbidden 에러 코드 (GlobalErrorCode에 추가 필요)
        GlobalErrorCode errorCode = GlobalErrorCode.AUTH_TOKEN_INVALID; // 임시로 사용
        
        log.warn("Access denied for {} {} - {}", 
                request.getMethod(), request.getRequestURI(), accessDeniedException.getMessage());
        
        // 에러 응답 생성
        ErrorResponse errorResponse = new ErrorResponse(
            403,
            "AUTH010", 
            "접근 권한이 없습니다.",
            "SECURITY",
            request.getRequestURI()
        );
        
        // JSON 응답 설정
        response.setStatus(403);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
