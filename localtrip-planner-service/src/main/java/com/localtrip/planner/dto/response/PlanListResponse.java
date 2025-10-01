package com.localtrip.planner.dto.response;

import com.localtrip.planner.entity.TravelPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 여행 계획 목록 응답 DTO (간략 정보)
 */
@Getter
@Builder
@AllArgsConstructor
public class PlanListResponse {

    /**
     * 여행 계획 ID
     */
    private Long planId;

    /**
     * 여행 이름
     */
    private String planName;

    /**
     * 인원수
     */
    private Integer peopleCnt;

    /**
     * 지역명
     */
    private String regionName;

    /**
     * 여행 시작일
     */
    private LocalDate startDate;

    /**
     * 여행 종료일
     */
    private LocalDate endDate;

    /**
     * 계획 생성일
     */
    private LocalDate planCreatedAt;

    /**
     * Entity -> DTO 변환
     */
    public static PlanListResponse from(TravelPlan plan) {
        return PlanListResponse.builder()
                .planId(plan.getId())
                .planName(plan.getPlanName())
                .peopleCnt(plan.getPeopleCnt())
                .regionName(plan.getRegionName())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .planCreatedAt(plan.getPlanCreatedAt())
                .build();
    }
}
