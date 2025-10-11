package com.localtrip.web.filter;

import com.localtrip.web.props.WebProps;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RequestLoggingFilter extends OncePerRequestFilter {
    
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private final WebProps webProps;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public RequestLoggingFilter(WebProps webProps) {
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

        long startTime = System.currentTimeMillis();
        
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logRequest(wrappedRequest, wrappedResponse, duration);
            wrappedResponse.copyBodyToResponse();
        }
    }

    private boolean isExcluded(String requestURI) {
        return Arrays.stream(webProps.getExcludePatterns())
                .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }

    private void logRequest(ContentCachingRequestWrapper request, 
                           ContentCachingResponseWrapper response, long duration) {
        
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("HTTP Request: ")
                 .append(request.getMethod())
                 .append(" ")
                 .append(request.getRequestURI());
        
        if (request.getQueryString() != null) {
            logMessage.append("?").append(request.getQueryString());
        }
        
        logMessage.append(" - Status: ").append(response.getStatus())
                 .append(" - Duration: ").append(duration).append("ms");

        // 요청 본문 로깅 (설정에 따라)
        if (webProps.isLogRequestBody() && request.getContentAsByteArray().length > 0) {
            String requestBody = getContentAsString(request.getContentAsByteArray(), 
                                                   webProps.getMaxBodySizeBytes());
            if (!requestBody.isEmpty()) {
                logMessage.append(" - Request Body: ").append(requestBody);
            }
        }

        log.info(logMessage.toString());
    }

    private String getContentAsString(byte[] content, int maxSize) {
        if (content.length == 0) {
            return "";
        }
        
        int length = Math.min(content.length, maxSize);
        return new String(content, 0, length, StandardCharsets.UTF_8);
    }
}
