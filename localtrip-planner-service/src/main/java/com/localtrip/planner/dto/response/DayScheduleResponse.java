package com.localtrip.planner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 일차별 일정 응답 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class DayScheduleResponse {

    /**
     * 일차
     */
    private Integer day;

    /**
     * 해당 일차의 장소 목록
     */
    private List<LocationDetailResponse> locations;
}
