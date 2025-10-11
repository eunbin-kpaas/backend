package com.localtrip.planner.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 일차별 일정 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DayScheduleDto {

    /**
     * 일차 (1일차, 2일차 등)
     */
    @NotNull(message = "일차는 필수입니다")
    @Positive(message = "일차는 1 이상이어야 합니다")
    private Integer daySeq;

    /**
     * 해당 일차의 장소 목록
     */
    @NotEmpty(message = "일정에는 최소 1개 이상의 장소가 필요합니다")
    @Valid
    private List<LocationDetailDto> scheduleByDay;
}
