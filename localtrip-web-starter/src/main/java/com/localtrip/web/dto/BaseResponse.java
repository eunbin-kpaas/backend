package com.localtrip.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * API 응답 표준화 클래스
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    
    private final boolean success;
    private final T data;
    private final String message;
    private final LocalDateTime timestamp;
    
    // 성공 응답 생성자
    private BaseResponse(T data, String message) {
        this.success = true;
        this.data = data;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    // 실패 응답 생성자
    private BaseResponse(String message) {
        this.success = false;
        this.data = null;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    // 성공 응답 생성 메서드들
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(data, null);
    }
    
    public static <T> BaseResponse<T> success(T data, String message) {
        return new BaseResponse<>(data, message);
    }
    
    public static BaseResponse<Void> success() {
        return new BaseResponse<>(null, "요청이 성공적으로 처리되었습니다.");
    }
    
    public static BaseResponse<Void> success(String message) {
        return new BaseResponse<>(null, message);
    }
    
    // 실패 응답 생성 메서드
    public static <T> BaseResponse<T> fail(String message) {
        return new BaseResponse<>(message);
    }
    
    // Getters
    public boolean isSuccess() {
        return success;
    }
    
    public T getData() {
        return data;
    }
    
    public String getMessage() {
        return message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
