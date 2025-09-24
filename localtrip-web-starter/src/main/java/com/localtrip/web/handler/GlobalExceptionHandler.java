package com.localtrip.web.handler;

import com.localtrip.common.exception.ErrorCode;
import com.localtrip.common.exception.ErrorResponse;
import com.localtrip.common.exception.GlobalErrorCode;
import com.localtrip.common.exception.custom.BusinessLogicException;
import com.localtrip.common.exception.custom.InternalServerException;
import com.localtrip.common.util.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

/**
 * 전역 예외 처리 핸들러
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 비즈니스 로직 예외 처리
     */
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogicException(
            BusinessLogicException e, HttpServletRequest request) {
        
        ErrorCode errorCode = e.getErrorCode();
        LogUtil.logError(log, request, errorCode, e);
        
        ErrorResponse errorResponse = ErrorResponse.from(errorCode, request.getRequestURI());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    /**
     * 서버 내부 오류 예외 처리
     */
    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerException(
            InternalServerException e, HttpServletRequest request) {
        
        ErrorCode errorCode = e.getErrorCode();
        LogUtil.logError(log, request, errorCode, e);
        
        ErrorResponse errorResponse = ErrorResponse.from(errorCode, request.getRequestURI());
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    /**
     * Bean Validation 예외 처리 (@Valid, @Validated)
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(
            BindException e, HttpServletRequest request) {
        
        // 필드 오류 메시지 수집
        String validationMessages = e.getBindingResult().getFieldErrors().stream()
                .map(this::getFieldErrorMessage)
                .collect(Collectors.joining(", "));
        
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        LogUtil.logError(log, request, errorCode, e);
        
        // 상세 메시지가 포함된 응답 생성
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatus(),
                errorCode.getCode(),
                errorCode.getMessage() + ": " + validationMessages,
                errorCode.getService(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 필수 파라미터 누락 예외 처리
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameterException(
            MissingServletRequestParameterException e, HttpServletRequest request) {
        
        GlobalErrorCode errorCode = GlobalErrorCode.MISSING_REQUIRED_PARAMETER;
        LogUtil.logError(log, request, errorCode, e);
        
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatus(),
                errorCode.getCode(),
                errorCode.getMessage() + ": " + e.getParameterName(),
                errorCode.getService(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * HTTP 메서드 지원하지 않음 예외 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        
        GlobalErrorCode errorCode = GlobalErrorCode.METHOD_NOT_ALLOWED;
        LogUtil.logError(log, request, errorCode, e);
        
        ErrorResponse errorResponse = ErrorResponse.from(errorCode, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    /**
     * 미디어 타입 지원하지 않음 예외 처리
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        
        GlobalErrorCode errorCode = GlobalErrorCode.UNSUPPORTED_MEDIA_TYPE;
        LogUtil.logError(log, request, errorCode, e);
        
        ErrorResponse errorResponse = ErrorResponse.from(errorCode, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse);
    }

    /**
     * 핸들러를 찾을 수 없음 예외 처리 (404)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            NoHandlerFoundException e, HttpServletRequest request) {
        
        GlobalErrorCode errorCode = GlobalErrorCode.RESOURCE_NOT_FOUND;
        LogUtil.logError(log, request, errorCode, e);
        
        ErrorResponse errorResponse = ErrorResponse.from(errorCode, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * 메서드 인자 타입 불일치 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        LogUtil.logError(log, request, errorCode, e);
        
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatus(),
                errorCode.getCode(),
                errorCode.getMessage() + ": " + e.getName(),
                errorCode.getService(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * JSON 파싱 예외 처리
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(
            HttpMessageNotReadableException e, HttpServletRequest request) {
        
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_INPUT_VALUE;
        LogUtil.logError(log, request, errorCode, e);
        
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getStatus(),
                errorCode.getCode(),
                "잘못된 JSON 형식입니다.",
                errorCode.getService(),
                request.getRequestURI()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 기타 모든 예외 처리 (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception e, HttpServletRequest request) {
        
        GlobalErrorCode errorCode = GlobalErrorCode.INTERNAL_SERVER_ERROR;
        LogUtil.logError(log, request, errorCode, e);
        
        ErrorResponse errorResponse = ErrorResponse.from(errorCode, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


    /**
     * 필드 오류 메시지 생성
     */
    private String getFieldErrorMessage(FieldError fieldError) {
        String field = fieldError.getField();
        String message = fieldError.getDefaultMessage();
        Object rejectedValue = fieldError.getRejectedValue();
        
        return String.format("%s: %s (입력값: %s)", field, message, rejectedValue);
    }
}
