package com.localtrip.planner.dto.response;

import com.localtrip.common.constants.PlaceType;
import com.localtrip.planner.entity.TravelPlanDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 장소 상세 정보 응답 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class LocationDetailResponse {

    /**
     * 순서
     */
    private Integer sequence;

    /**
     * Google Place ID
     */
    private String googlePlaceId;

    /**
     * 장소 이름
     */
    private String placeName;

    /**
     * 장소 타입
     */
    private PlaceType placeType;

    /**
     * 장소 주소
     */
    private String address;

    /**
     * 위도
     */
    private Double latitude;

    /**
     * 경도
     */
    private Double longitude;

    /**
     * Entity -> DTO 변환
     */
    public static LocationDetailResponse from(TravelPlanDetail detail) {
        return LocationDetailResponse.builder()
                .sequence(detail.getSequence())
                .googlePlaceId(detail.getGooglePlaceId())
                .placeName(detail.getPlaceName())
                .placeType(detail.getPlaceTypeEnum())
                .address(detail.getPlaceAddr())
                .latitude(detail.getLatitude())
                .longitude(detail.getLongitude())
                .build();
    }
}
