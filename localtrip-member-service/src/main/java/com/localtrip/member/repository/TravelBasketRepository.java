package com.localtrip.member.repository;

import com.localtrip.member.entity.TravelBasket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelBasketRepository extends JpaRepository<TravelBasket, Long> {
    
    Optional<TravelBasket> findByMemberId(Long memberId);
}
