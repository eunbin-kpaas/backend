package com.localtrip.planner.service;

import com.localtrip.common.exception.custom.BusinessLogicException;
import com.localtrip.planner.dto.request.CreatePlanRequest;
import com.localtrip.planner.dto.request.DayScheduleDto;
import com.localtrip.planner.dto.request.LocationDetailDto;
import com.localtrip.planner.dto.request.UpdatePlanRequest;
import com.localtrip.planner.dto.response.PlanListResponse;
import com.localtrip.planner.dto.response.PlanResponse;
import com.localtrip.planner.entity.TravelPlan;
import com.localtrip.planner.entity.TravelPlanDetail;
import com.localtrip.planner.exception.PlannerErrorCode;
import com.localtrip.planner.repository.TravelPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 여행 계획 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelPlanService {

    private final TravelPlanRepository travelPlanRepository;

    /**
     * 여행 계획 생성
     */
    @Transactional
    public PlanResponse createPlan(Long memberId, CreatePlanRequest request) {
        log.info("Creating travel plan for memberId: {}, planName: {}", memberId, request.getScheduleName());

        // 여행 계획 생성
        TravelPlan plan = TravelPlan.builder()
                .memberId(memberId)
                .planName(request.getScheduleName())
                .peopleCnt(request.getHowManyPeople())
                .regionName(request.getRegionName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        // 상세 일정 추가
        request.getDetailSchedule().forEach(daySchedule -> {
            daySchedule.getScheduleByDay().forEach(location -> {
                TravelPlanDetail detail = TravelPlanDetail.builder()
                        .travelPlan(plan)
                        .day(daySchedule.getDaySeq())
                        .sequence(location.getLocationSeq())
                        .placeName(location.getPlaceName())
                        .placeAddr(location.getAddress())
                        .placeType(location.getPlaceType())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .googlePlaceId(location.getPlaceId())
                        .build();
                plan.addDetail(detail);
            });
        });

        TravelPlan savedPlan = travelPlanRepository.save(plan);
        log.info("Travel plan created successfully. planId: {}", savedPlan.getId());

        return PlanResponse.from(savedPlan);
    }

    /**
     * 여행 계획 조회
     */
    public PlanResponse getPlan(Long memberId, Long planId) {
        log.info("Fetching travel plan. memberId: {}, planId: {}", memberId, planId);

        TravelPlan plan = travelPlanRepository.findByIdAndMemberIdWithDetails(planId, memberId)
                .orElseThrow(() -> new BusinessLogicException(PlannerErrorCode.PLAN_NOT_FOUND));

        return PlanResponse.from(plan);
    }

    /**
     * 내 여행 계획 목록 조회
     */
    public List<PlanListResponse> getMyPlans(Long memberId) {
        log.info("Fetching travel plan list for memberId: {}", memberId);

        List<TravelPlan> plans = travelPlanRepository.findByMemberIdOrderByPlanCreatedAtDesc(memberId);

        return plans.stream()
                .map(PlanListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 여행 계획 수정
     */
    @Transactional
    public PlanResponse updatePlan(Long memberId, Long planId, UpdatePlanRequest request) {
        log.info("Updating travel plan. memberId: {}, planId: {}", memberId, planId);

        // 여행 계획 조회 및 권한 체크
        TravelPlan plan = travelPlanRepository.findByIdAndMemberIdWithDetails(planId, memberId)
                .orElseThrow(() -> new BusinessLogicException(PlannerErrorCode.PLAN_NOT_FOUND));

        // 기본 정보 수정
        plan.update(
                request.getScheduleName(),
                request.getHowManyPeople(),
                request.getRegionName(),
                request.getStartDate(),
                request.getEndDate()
        );

        // 상세 일정 재생성
        List<TravelPlanDetail> newDetails = request.getDetailSchedule().stream()
                .flatMap(daySchedule ->
                        daySchedule.getScheduleByDay().stream()
                                .map(location -> TravelPlanDetail.builder()
                                        .day(daySchedule.getDaySeq())
                                        .sequence(location.getLocationSeq())
                                        .placeName(location.getPlaceName())
                                        .placeAddr(location.getAddress())
                                        .placeType(location.getPlaceType())
                                        .latitude(location.getLatitude())
                                        .longitude(location.getLongitude())
                                        .googlePlaceId(location.getPlaceId())
                                        .build())
                )
                .collect(Collectors.toList());

        plan.setDetails(newDetails);

        log.info("Travel plan updated successfully. planId: {}", planId);

        return PlanResponse.from(plan);
    }

    /**
     * 여행 계획 삭제
     */
    @Transactional
    public void deletePlan(Long memberId, Long planId) {
        log.info("Deleting travel plan. memberId: {}, planId: {}", memberId, planId);

        // 여행 계획 조회 및 권한 체크
        TravelPlan plan = travelPlanRepository.findByIdAndMemberId(planId, memberId)
                .orElseThrow(() -> new BusinessLogicException(PlannerErrorCode.PLAN_NOT_FOUND));

        travelPlanRepository.delete(plan);

        log.info("Travel plan deleted successfully. planId: {}", planId);
    }
}
