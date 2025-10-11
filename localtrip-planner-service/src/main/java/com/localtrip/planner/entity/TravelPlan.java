package com.localtrip.planner.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 여행 계획 엔티티
 */
@Entity
@Table(name = "travel_plan")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 회원 ID (member-service의 Member.id)
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 여행 이름
     */
    @Column(name = "plan_name", nullable = false, length = 30)
    private String planName;

    /**
     * 인원수
     */
    @Column(name = "people_cnt", nullable = false)
    private Integer peopleCnt;

    /**
     * 지역명 (예: 서울, 부산)
     */
    @Column(name = "region_name", nullable = false, length = 50)
    private String regionName;

    /**
     * 여행 시작 날짜
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * 여행 끝 날짜
     */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * 계획 생성일
     */
    @Column(name = "plan_created_at", nullable = false, updatable = false)
    private LocalDate planCreatedAt;

    /**
     * 여행 계획 상세 목록
     */
    @OneToMany(mappedBy = "travelPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TravelPlanDetail> details = new ArrayList<>();

    @Builder
    public TravelPlan(Long memberId, String planName, Integer peopleCnt, String regionName,
                      LocalDate startDate, LocalDate endDate) {
        this.memberId = memberId;
        this.planName = planName;
        this.peopleCnt = peopleCnt;
        this.regionName = regionName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.planCreatedAt = LocalDate.now();
    }

    /**
     * 여행 계획 상세 추가
     */
    public void addDetail(TravelPlanDetail detail) {
        this.details.add(detail);
        detail.setTravelPlan(this);
    }

    /**
     * 여행 계획 상세 전체 설정 (수정 시 사용)
     */
    public void setDetails(List<TravelPlanDetail> newDetails) {
        this.details.clear();
        newDetails.forEach(this::addDetail);
    }

    /**
     * 여행 계획 수정
     */
    public void update(String planName, Integer peopleCnt, String regionName,
                       LocalDate startDate, LocalDate endDate) {
        this.planName = planName;
        this.peopleCnt = peopleCnt;
        this.regionName = regionName;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
