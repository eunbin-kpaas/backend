package com.localtrip.web.filter;

import com.localtrip.web.props.WebProps;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

/**
 * 전역 요청 로깅 필터 (IP, URI, 소요시간 등 상세 로깅)
 */
public class GlobalLoggingFilter extends OncePerRequestFilter {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalLoggingFilter.class);
    private final WebProps webProps;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    
    // MDC 키 상수들
    private static final String REQUEST_ID = "requestId";
    private static final String CLIENT_IP = "clientIp";
    private static final String USER_AGENT = "userAgent";
    private static final String REQUEST_URI = "requestUri";

    public GlobalLoggingFilter(WebProps webProps) {
        this.webProps = webProps;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 샘플링 체크
        if (Math.random() > webProps.getSampleRate()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 제외 패턴 체크
        if (isExcluded(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        // Request ID 생성 및 MDC 설정
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        String clientIp = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        MDC.put(REQUEST_ID, requestId);
        MDC.put(CLIENT_IP, clientIp);
        MDC.put(USER_AGENT, userAgent != null ? userAgent : "Unknown");
        MDC.put(REQUEST_URI, request.getRequestURI());
        
        // 응답 헤더에 Request ID 추가
        response.setHeader("X-Request-ID", requestId);

        long startTime = System.currentTimeMillis();
        
        try {
            // 요청 시작 로그
            logRequestStart(wrappedRequest);
            
            filterChain.doFilter(wrappedRequest, wrappedResponse);
            
        } catch (Exception e) {
            // 예외 발생 시 소요시간만 기록 (성능 문제 탐지용)
            long duration = System.currentTimeMillis() - startTime;
            if (duration > 1000) {
                log.warn("🐌 [EXCEPTION_SLOW] {} {} | Duration: {}ms", 
                         wrappedRequest.getMethod(), wrappedRequest.getRequestURI(), duration);
            }
            throw e;
        } finally {
            // 정상 완료된 경우에만 COMPLETE 로그
            if (wrappedResponse.getStatus() > 0) { // 응답이 정상적으로 설정된 경우
                long duration = System.currentTimeMillis() - startTime;
                logRequestComplete(wrappedRequest, wrappedResponse, duration);
            }
            
            // 정리 작업은 항상 수행
            wrappedResponse.copyBodyToResponse();
            
            // MDC 정리
            MDC.clear();
        }
    }
    
    /**
     * 요청 시작 로그
     */
    private void logRequestStart(HttpServletRequest request) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("[REQUEST START] ")
                 .append(request.getMethod())
                 .append(" ")
                 .append(request.getRequestURI());
        
        if (request.getQueryString() != null) {
            logMessage.append("?").append(request.getQueryString());
        }
        
        logMessage.append(" | IP: ").append(MDC.get(CLIENT_IP))
                 .append(" | UserAgent: ").append(sanitizeUserAgent(MDC.get(USER_AGENT)));
        
        log.info(logMessage.toString());
    }
    
    /**
     * 요청 완료 로그
     */
    private void logRequestComplete(ContentCachingRequestWrapper request, 
                                   ContentCachingResponseWrapper response, long duration) {
        
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("[REQUEST COMPLETE] ")
                 .append(request.getMethod())
                 .append(" ")
                 .append(request.getRequestURI())
                 .append(" | Status: ").append(response.getStatus())
                 .append(" | Duration: ").append(duration).append("ms")
                 .append(" | Size: ").append(response.getContentSize()).append(" bytes");

        // 요청 본문 로깅 (설정에 따라)
        if (webProps.isLogRequestBody() && request.getContentAsByteArray().length > 0) {
            String requestBody = getContentAsString(request.getContentAsByteArray(), 
                                                   webProps.getMaxBodySizeBytes());
            if (!requestBody.isEmpty()) {
                logMessage.append(" | RequestBody: ").append(sanitizeBody(requestBody));
            }
        }
        
        // 성능 기준에 따른 로그 레벨 조정
        if (duration > 3000) {
            log.warn("⚠️ VERY_SLOW_API: " + logMessage);
        } else if (duration > 1000) {
            log.warn("🐌 SLOW_API: " + logMessage);
        } else {
            log.info("✅ " + logMessage);
        }
    }
    
    /**
     * 클라이언트 IP 주소 추출
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "X-Real-IP", 
            "X-Original-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };
        
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // 첫 번째 IP만 사용 (프록시 체인의 경우)
                return ip.split(",")[0].trim();
            }
        }
        
        return request.getRemoteAddr();
    }
    
    /**
     * User-Agent 정리 (너무 긴 경우 축약)
     */
    private String sanitizeUserAgent(String userAgent) {
        if (userAgent == null) return "Unknown";
        return userAgent.length() > 100 ? userAgent.substring(0, 100) + "..." : userAgent;
    }
    
    /**
     * 요청/응답 본문 정리 (민감정보 마스킹)
     */
    private String sanitizeBody(String body) {
        if (body == null) return "";
        
        // 비밀번호, 토큰 등 민감정보 마스킹
        return body.replaceAll("(?i)(password|token|key|secret)\"\\s*:\\s*\"[^\"]*\"", "$1\":\"***\"");
    }

    private boolean isExcluded(String requestURI) {
        return Arrays.stream(webProps.getExcludePatterns())
                .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }

    private String getContentAsString(byte[] content, int maxSize) {
        if (content.length == 0) {
            return "";
        }
        
        int length = Math.min(content.length, maxSize);
        return new String(content, 0, length, StandardCharsets.UTF_8);
    }
}
