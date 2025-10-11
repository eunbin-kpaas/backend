package com.localtrip.planner.controller;

import com.localtrip.auth.model.AuthenticatedUser;
import com.localtrip.planner.dto.request.CreatePlanRequest;
import com.localtrip.planner.dto.request.UpdatePlanRequest;
import com.localtrip.planner.dto.response.PlanListResponse;
import com.localtrip.planner.dto.response.PlanResponse;
import com.localtrip.planner.service.TravelPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 여행 계획 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class TravelPlanController {

    private final TravelPlanService travelPlanService;

    /**
     * 여행 계획 생성
     *
     * @param user 인증된 사용자 정보
     * @param request 여행 계획 생성 요청
     * @return 생성된 여행 계획 정보
     */
    @PostMapping
    public ResponseEntity<PlanResponse> createPlan(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody CreatePlanRequest request) {
        
        Long memberId = Long.parseLong(user.getUserId());
        log.info("POST /api/plans - memberId: {}", memberId);

        PlanResponse response = travelPlanService.createPlan(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 여행 계획 조회
     *
     * @param user 인증된 사용자 정보
     * @param planId 여행 계획 ID
     * @return 여행 계획 상세 정보
     */
    @GetMapping("/{planId}")
    public ResponseEntity<PlanResponse> getPlan(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long planId) {
        
        Long memberId = Long.parseLong(user.getUserId());
        log.info("GET /api/plans/{} - memberId: {}", planId, memberId);

        PlanResponse response = travelPlanService.getPlan(memberId, planId);
        return ResponseEntity.ok(response);
    }

    /**
     * 내 여행 계획 목록 조회
     *
     * @param user 인증된 사용자 정보
     * @return 여행 계획 목록
     */
    @GetMapping
    public ResponseEntity<List<PlanListResponse>> getMyPlans(
            @AuthenticationPrincipal AuthenticatedUser user) {
        
        Long memberId = Long.parseLong(user.getUserId());
        log.info("GET /api/plans - memberId: {}", memberId);

        List<PlanListResponse> response = travelPlanService.getMyPlans(memberId);
        return ResponseEntity.ok(response);
    }

    /**
     * 여행 계획 수정
     *
     * @param user 인증된 사용자 정보
     * @param planId 여행 계획 ID
     * @param request 여행 계획 수정 요청
     * @return 수정된 여행 계획 정보
     */
    @PutMapping("/{planId}")
    public ResponseEntity<PlanResponse> updatePlan(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long planId,
            @Valid @RequestBody UpdatePlanRequest request) {
        
        Long memberId = Long.parseLong(user.getUserId());
        log.info("PUT /api/plans/{} - memberId: {}", planId, memberId);

        PlanResponse response = travelPlanService.updatePlan(memberId, planId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 여행 계획 삭제
     *
     * @param user 인증된 사용자 정보
     * @param planId 여행 계획 ID
     * @return 삭제 완료
     */
    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long planId) {
        
        Long memberId = Long.parseLong(user.getUserId());
        log.info("DELETE /api/plans/{} - memberId: {}", planId, memberId);

        travelPlanService.deletePlan(memberId, planId);
        return ResponseEntity.noContent().build();
    }
}
