package com.localtrip.member.repository;

import com.localtrip.member.entity.TravelBasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TravelBasketItemRepository extends JpaRepository<TravelBasketItem, Long> {
    
    List<TravelBasketItem> findByBasketId(Long basketId);
    
    @Query("SELECT t FROM TravelBasketItem t WHERE t.basketId = :basketId ORDER BY t.region, t.createdAt DESC")
    List<TravelBasketItem> findByBasketIdOrderByRegionAndCreatedAt(@Param("basketId") Long basketId);
    
    void deleteByBasketIdAndId(Long basketId, Long itemId);
}
