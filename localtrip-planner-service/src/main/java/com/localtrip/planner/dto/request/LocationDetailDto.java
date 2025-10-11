package com.localtrip.planner.dto.request;

import com.localtrip.common.constants.PlaceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 장소 상세 정보 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDetailDto {

    /**
     * 해당 일차 내 순서
     */
    @NotNull(message = "순서는 필수입니다")
    @Positive(message = "순서는 1 이상이어야 합니다")
    private Integer locationSeq;

    /**
     * Google Place ID (nullable)
     */
    private String placeId;

    /**
     * 장소 이름
     */
    @NotBlank(message = "장소 이름은 필수입니다")
    private String placeName;

    /**
     * 장소 타입 (ATTRACTION, RESTAURANT, ACCOMMODATION, ETC)
     */
    @NotNull(message = "장소 타입은 필수입니다")
    private PlaceType placeType;

    /**
     * 장소 주소
     */
    @NotBlank(message = "장소 주소는 필수입니다")
    private String address;

    /**
     * 위도
     */
    @NotNull(message = "위도는 필수입니다")
    private Double latitude;

    /**
     * 경도
     */
    @NotNull(message = "경도는 필수입니다")
    private Double longitude;
}
