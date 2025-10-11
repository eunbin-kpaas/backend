package com.localtrip.planner.repository;

import com.localtrip.planner.entity.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 여행 계획 Repository
 */
public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {

    /**
     * 회원별 여행 계획 목록 조회 (최신순)
     */
    List<TravelPlan> findByMemberIdOrderByPlanCreatedAtDesc(Long memberId);

    /**
     * 여행 계획 ID와 회원 ID로 조회 (권한 체크용)
     */
    Optional<TravelPlan> findByIdAndMemberId(Long id, Long memberId);

    /**
     * 여행 계획 상세 정보 포함 조회 (N+1 문제 방지)
     */
    @Query("SELECT DISTINCT p FROM TravelPlan p " +
           "LEFT JOIN FETCH p.details " +
           "WHERE p.id = :planId AND p.memberId = :memberId")
    Optional<TravelPlan> findByIdAndMemberIdWithDetails(@Param("planId") Long planId, 
                                                          @Param("memberId") Long memberId);
}
