package com.localtrip.member.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.localtrip.common.exception.ErrorCode;

/**
 * 회원 도메인 ErrorCode
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MemberErrorCode implements ErrorCode {
    
    // 로그인 관련 (MEM***)  
    INVALID_LOGIN_CREDENTIALS(401, "MEM001", "아이디 또는 비밀번호가 올바르지 않습니다.", "MEMBER"),
    MEMBER_NOT_FOUND(404, "MEM002", "존재하지 않는 회원입니다.", "MEMBER"),
    
    // 회원가입 관련
    MEMBER_ID_DUPLICATED(409, "MEM003", "이미 사용중인 아이디입니다.", "MEMBER"),
    EMAIL_DUPLICATED(409, "MEM004", "이미 사용중인 이메일입니다.", "MEMBER"),
    
    // 이메일 인증 관련
    VERIFICATION_CODE_INVALID(400, "MEM005", "인증코드가 올바르지 않습니다.", "MEMBER"),
    VERIFICATION_CODE_EXPIRED(400, "MEM006", "인증코드가 만료되었습니다.", "MEMBER"),
    VERIFICATION_CODE_NOT_FOUND(400, "MEM007", "인증코드를 찾을 수 없습니다.", "MEMBER"),
    EMAIL_SEND_FAILED(500, "MEM008", "이메일 발송에 실패했습니다.", "MEMBER"),
    
    // JWT 토큰 관련
    INVALID_TOKEN(401, "MEM009", "유효하지 않은 토큰입니다.", "MEMBER"),
    TOKEN_EXPIRED(401, "MEM010", "만료된 토큰입니다.", "MEMBER"),
    REFRESH_TOKEN_INVALID(401, "MEM011", "유효하지 않은 리프레시 토큰입니다.", "MEMBER");
    
    private final int status;
    private final String code;
    private final String message;
    private final String service;
    
    MemberErrorCode(int status, String code, String message, String service) {
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
