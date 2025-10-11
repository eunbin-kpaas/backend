package com.localtrip.common.exception.custom;

import com.localtrip.common.exception.ErrorCode;

public class BusinessLogicException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String detailMessage;

    // 생성자: 예외 코드만 사용하는 경우
    public BusinessLogicException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detailMessage = null;
    }

    // 생성자: 예외 코드와 추가적인 메시지를 함께 사용하는 경우
    public BusinessLogicException(ErrorCode errorCode, String detailMessage) {
        super(errorCode.getMessage() + ": " + detailMessage);
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
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

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this; // 성능 최적화: 스택 트레이스 생략
    }
}
