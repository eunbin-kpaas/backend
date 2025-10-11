package com.localtrip.common.util;

import com.localtrip.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.server.ServerHttpRequest;

/**
 * 로그 작성 유틸리티 클래스
 */
public class LogUtil {

    /**
     * 오류 로그 기록
     */
    public static void logError(Logger logger, HttpServletRequest request, ErrorCode errorCode, Throwable ex) {
        if (ex != null) {
            Throwable root = NestedExceptionUtils.getMostSpecificCause(ex);
            logger.error("[❌ ERROR {}] {} {} | code={}, message={}, exception={}, rootCause={} - {}",
                    errorCode.getService(),
                    request.getMethod(),
                    request.getRequestURI(),
                    errorCode.getCode(),
                    errorCode.getMessage(),
                    ex.getMessage(),
                    (root != null ? root.getClass().getSimpleName() : "n/a"),
                    (root != null ? root.getMessage() : "n/a"),
                    ex // 전체 스택 출력
            );
        } else {
            logger.error("[❌ ERROR {}] {} {} | code={}, message={}",
                    errorCode.getService(),
                    request.getMethod(),
                    request.getRequestURI(),
                    errorCode.getCode(),
                    errorCode.getMessage()
            );
        }
    }

    /**
     * 경고 로그 기록
     */
    public static void logWarn(Logger logger, HttpServletRequest request, ErrorCode errorCode, Throwable ex) {
        if (ex != null) {
            logger.warn("[❗ WARN {}] {} {} | code={}, message={}, exception={}",
                    errorCode.getService(),
                    request.getMethod(),
                    request.getRequestURI(),
                    errorCode.getCode(),
                    errorCode.getMessage(),
                    ex.getMessage()
            );
        } else {
            logger.warn("[❗ WARN {}] {} {} | code={}, message={}",
                    errorCode.getService(),
                    request.getMethod(),
                    request.getRequestURI(),
                    errorCode.getCode(),
                    errorCode.getMessage()
            );
        }
    }

    /**
     * API 요청 처리 시간 로그 기록
     */
    public static void logRequestDuration(Logger logger, ServerHttpRequest request, long duration) {
        String method = request.getMethod() != null ? request.getMethod().name() : "UNKNOWN";
        String uri = request.getURI().getPath();

        if (duration > 1000) {
            logger.warn("[SLOW API] {} {} took {}ms", method, uri, duration);
        } else {
            logger.info("[API] {} {} took {}ms", method, uri, duration);
        }
    }

    /**
     * 비즈니스 로직 성공 로그 기록
     */
    public static void logSuccess(Logger logger, String operation, Object... params) {
        logger.info("[✅ SUCCESS] {} | params={}", operation, params);
    }

    /**
     * 외부 API 호출 로그 기록
     */
    public static void logExternalApi(Logger logger, String apiName, String method, String url, long duration) {
        logger.info("[🌐 EXTERNAL] {} {} {} took {}ms", apiName, method, url, duration);
    }

    /**
     * 캐시 관련 로그 기록
     */
    public static void logCache(Logger logger, String operation, String key, boolean hit) {
        if (hit) {
            logger.debug("[💾 CACHE HIT] {} | key={}", operation, key);
        } else {
            logger.debug("[💾 CACHE MISS] {} | key={}", operation, key);
        }
    }
}
