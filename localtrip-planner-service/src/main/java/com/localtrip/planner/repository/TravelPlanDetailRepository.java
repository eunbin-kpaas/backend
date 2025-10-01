package com.localtrip.planner.repository;

import com.localtrip.planner.entity.TravelPlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 여행 계획 상세 Repository
 */
public interface TravelPlanDetailRepository extends JpaRepository<TravelPlanDetail, Long> {
}
