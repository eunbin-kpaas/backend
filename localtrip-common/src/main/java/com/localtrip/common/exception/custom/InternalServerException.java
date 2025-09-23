package com.localtrip.common.exception.custom;

import com.localtrip.common.exception.ErrorCode;

public class InternalServerException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detailMessage;

    // 생성자: 예외 코드만 사용하는 경우
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = null;
    }

    // 생성자: 예외 코드와 추가적인 메시지를 함께 사용하는 경우
    public InternalServerException(ErrorCode errorCode, String detailMessage) {
        super(errorCode.getMessage() + ": " + detailMessage);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    // 생성자: 원인 예외와 함께 사용하는 경우
    public InternalServerException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.detailMessage = null;
    }

    // 예외 코드 반환
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    // HTTP 상태 코드 반환
    public int getStatus() {
        return errorCode.getStatus();
    }

    // 세부 메시지 반환
    public String getDetailMessage() {
        return detailMessage;
    }
}
