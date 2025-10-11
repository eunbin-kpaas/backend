package com.localtrip.planner.exception;

import com.localtrip.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Planner 서비스 에러 코드
 */
@Getter
@RequiredArgsConstructor
public enum PlannerErrorCode implements ErrorCode {

    // 여행 계획 관련 (4000~4099)
    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAN_4000", "여행 계획을 찾을 수 없습니다."),
    PLAN_ACCESS_DENIED(HttpStatus.FORBIDDEN, "PLAN_4001", "해당 여행 계획에 접근 권한이 없습니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "PLAN_4002", "여행 종료일은 시작일 이후여야 합니다."),
    INVALID_DAY_SEQUENCE(HttpStatus.BAD_REQUEST, "PLAN_4003", "일차 정보가 올바르지 않습니다."),
    EMPTY_SCHEDULE(HttpStatus.BAD_REQUEST, "PLAN_4004", "여행 일정이 비어있습니다."),
    INVALID_LOCATION_SEQUENCE(HttpStatus.BAD_REQUEST, "PLAN_4005", "장소 순서 정보가 올바르지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public int getStatus() {
        return httpStatus.value();
    }

    @Override
    public String getService() {
        return "planner-service";
    }
}
