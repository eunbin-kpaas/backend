package com.localtrip.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GlobalErrorCode implements ErrorCode {

    // 시스템/기술적 예외 (SYS***)
    INTERNAL_SERVER_ERROR(500, "SYS000", "서버 내부 오류가 발생했습니다.", "GLOBAL"),
    DATABASE_ERROR(500, "SYS001", "데이터베이스 처리 중 오류가 발생했습니다.", "GLOBAL"),
    REDIS_CONNECTION_ERROR(500, "SYS002", "캐시 서버 연결에 실패했습니다.", "GLOBAL"),
    FILE_UPLOAD_FAILED(500, "SYS003", "파일 업로드에 실패했습니다.", "GLOBAL"),
    EMAIL_SERVICE_DOWN(503, "SYS004", "이메일 서비스가 현재 사용할 수 없습니다.", "GLOBAL"),

    // 인증/인가 관련 (AUTH***)
    AUTH_HEADER_MISSING(401, "AUTH001", "인증 토큰이 존재하지 않습니다.", "SECURITY"),
    AUTH_TOKEN_MALFORMED(400, "AUTH002", "토큰 형식이 올바르지 않습니다.", "SECURITY"),
    AUTH_TOKEN_EXPIRED(401, "AUTH003", "액세스 토큰이 만료되었습니다.", "SECURITY"),
    AUTH_TOKEN_INVALID(401, "AUTH004", "유효하지 않은 액세스 토큰입니다.", "SECURITY"),
    AUTH_TOKEN_LOGGED_OUT(401, "AUTH005", "이미 로그아웃된 토큰입니다.", "SECURITY"),
    AUTH_USER_NOT_FOUND(401, "AUTH006", "인증 대상 사용자를 찾을 수 없습니다.", "SECURITY"),
    AUTH_REDIS_ERROR(503, "AUTH007", "세션 검증 중 캐시 서버 오류가 발생했습니다.", "SECURITY"),
    AUTH_SECURITY_CONTEXT_ERROR(500, "AUTH008", "보안 컨텍스트 설정 중 오류가 발생했습니다.", "SECURITY"),
    AUTH_INTERNAL(500, "AUTH009", "인증 처리 중 내부 오류가 발생했습니다.", "SECURITY"),

    // 공통 클라이언트 오류 (CLT***)
    INVALID_INPUT_VALUE(400, "CLT001", "유효하지 않은 입력입니다.", "GLOBAL"),
    MISSING_REQUIRED_PARAMETER(400, "CLT002", "필수 파라미터가 누락되었습니다.", "GLOBAL"),
    UNSUPPORTED_MEDIA_TYPE(415, "CLT003", "지원하지 않는 미디어 타입입니다.", "GLOBAL"),
    RESOURCE_NOT_FOUND(404, "CLT004", "요청한 리소스를 찾을 수 없습니다.", "GLOBAL"),
    METHOD_NOT_ALLOWED(405, "CLT005", "허용되지 않은 HTTP 메서드입니다.", "GLOBAL"),

    // 외부 API 공통 오류 (EXT***)
    EXTERNAL_API_CONNECTION_ERROR(503, "EXT000", "외부 API 연결에 실패했습니다.", "GLOBAL"),
    EXTERNAL_API_RATE_LIMIT(429, "EXT001", "외부 API 호출 한도를 초과했습니다.", "GLOBAL"),
    EXTERNAL_API_TIMEOUT(504, "EXT002", "외부 API 응답 시간이 초과되었습니다.", "GLOBAL")
    ;

    private final int status;
    private final String code;
    private final String message;
    private final String service;

    GlobalErrorCode(int status, String code, String message, String service) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.service = service;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getService() {
        return service;
    }
}
