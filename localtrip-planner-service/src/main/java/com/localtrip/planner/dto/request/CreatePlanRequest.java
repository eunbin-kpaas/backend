package com.localtrip.planner.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 여행 계획 생성 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlanRequest {

    /**
     * 여행 계획 이름
     */
    @NotBlank(message = "여행 이름은 필수입니다")
    @Size(max = 30, message = "여행 이름은 30자 이하여야 합니다")
    private String scheduleName;

    /**
     * 여행 시작일
     */
    @NotNull(message = "여행 시작일은 필수입니다")
    private LocalDate startDate;

    /**
     * 여행 종료일
     */
    @NotNull(message = "여행 종료일은 필수입니다")
    private LocalDate endDate;

    /**
     * 인원수
     */
    @NotNull(message = "인원수는 필수입니다")
    @Positive(message = "인원수는 1명 이상이어야 합니다")
    private Integer howManyPeople;

    /**
     * 지역명 (예: 서울, 부산)
     */
    @NotBlank(message = "지역명은 필수입니다")
    @Size(max = 50, message = "지역명은 50자 이하여야 합니다")
    private String regionName;

    /**
     * 상세 일정 목록
     */
    @NotEmpty(message = "상세 일정은 필수입니다")
    @Valid
    private List<DayScheduleDto> detailSchedule;

    /**
     * 여행 시작일이 종료일보다 이전인지 검증
     */
    @AssertTrue(message = "여행 종료일은 시작일 이후여야 합니다")
    public boolean isValidDateRange() {
        if (startDate == null || endDate == null) {
            return true; // null 검증은 @NotNull에서 처리
        }
        return !endDate.isBefore(startDate);
    }
}
