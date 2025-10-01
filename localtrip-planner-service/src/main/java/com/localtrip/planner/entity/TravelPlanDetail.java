package com.localtrip.planner.entity;

import com.localtrip.common.constants.PlaceType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 여행 계획 상세 정보 엔티티
 */
@Entity
@Table(name = "travel_plan_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelPlanDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 여행 계획 (외래키)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private TravelPlan travelPlan;

    /**
     * 일차 (1일차, 2일차 등)
     */
    @Column(name = "day", nullable = false)
    private Integer day;

    /**
     * 순서 (해당 일차 내에서의 순서)
     */
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    /**
     * 장소 이름
     */
    @Column(name = "place_name", nullable = false)
    private String placeName;

    /**
     * 장소 주소
     */
    @Column(name = "place_addr", nullable = false)
    private String placeAddr;

    /**
     * 장소 타입 (관광명소, 맛집, 숙소, 기타)
     * DB에는 int로 저장
     */
    @Column(name = "place_type", nullable = false)
    private Integer placeType;

    /**
     * 위도
     */
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    /**
     * 경도
     */
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    /**
     * Google Place ID (nullable)
     */
    @Column(name = "google_place_id")
    private String googlePlaceId;

    @Builder
    public TravelPlanDetail(TravelPlan travelPlan, Integer day, Integer sequence,
                            String placeName, String placeAddr, PlaceType placeType,
                            Double latitude, Double longitude, String googlePlaceId) {
        this.travelPlan = travelPlan;
        this.day = day;
        this.sequence = sequence;
        this.placeName = placeName;
        this.placeAddr = placeAddr;
        this.placeType = placeType.getCode();
        this.latitude = latitude;
        this.longitude = longitude;
        this.googlePlaceId = googlePlaceId;
    }

    /**
     * TravelPlan 설정 (양방향 관계 설정용)
     */
    protected void setTravelPlan(TravelPlan travelPlan) {
        this.travelPlan = travelPlan;
    }

    /**
     * PlaceType enum 반환
     */
    public PlaceType getPlaceTypeEnum() {
        return PlaceType.fromCode(this.placeType);
    }
}
