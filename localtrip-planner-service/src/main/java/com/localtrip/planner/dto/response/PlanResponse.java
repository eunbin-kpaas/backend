package com.localtrip.planner.dto.response;

import com.localtrip.planner.entity.TravelPlan;
import com.localtrip.planner.entity.TravelPlanDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 여행 계획 상세 응답 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class PlanResponse {

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
     * 일차별 상세 일정
     */
    private List<DayScheduleResponse> detailSchedule;

    /**
     * Entity -> DTO 변환
     */
    public static PlanResponse from(TravelPlan plan) {
        // 일차별로 그룹화
        Map<Integer, List<TravelPlanDetail>> detailsByDay = plan.getDetails().stream()
                .collect(Collectors.groupingBy(TravelPlanDetail::getDay));

        // 일차별 응답 생성 (일차 순서대로 정렬)
        List<DayScheduleResponse> schedules = detailsByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Integer day = entry.getKey();
                    List<LocationDetailResponse> locations = entry.getValue().stream()
                            .sorted((d1, d2) -> d1.getSequence().compareTo(d2.getSequence()))
                            .map(LocationDetailResponse::from)
                            .collect(Collectors.toList());

                    return DayScheduleResponse.builder()
                            .day(day)
                            .locations(locations)
                            .build();
                })
                .collect(Collectors.toList());

        return PlanResponse.builder()
                .planId(plan.getId())
                .planName(plan.getPlanName())
                .peopleCnt(plan.getPeopleCnt())
                .regionName(plan.getRegionName())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .planCreatedAt(plan.getPlanCreatedAt())
                .detailSchedule(schedules)
                .build();
    }
}
