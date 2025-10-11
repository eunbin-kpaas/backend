package com.localtrip.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 여행바구니 아이템 (장소 정보)
 */
@Entity
@Table(name = "travel_basket_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelBasketItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "basket_id", nullable = false)
    private Long basketId;
    
    @Column(name = "region", nullable = false)
    private String region;  // 지역명 (서울, 부산 등)
    
    @Column(name = "place_name", nullable = false)
    private String placeName;  // 장소 이름
    
    @Column(name = "place_addr")
    private String placeAddr;  // 장소 주소
    
    @Column(name = "img_url")
    private String imgUrl;  // 이미지 URL (S3 등 클라우드 객체 스토리지)
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    @Builder
    public TravelBasketItem(Long basketId, String region, String placeName, 
                           String placeAddr, String imgUrl) {
        this.basketId = basketId;
        this.region = region;
        this.placeName = placeName;
        this.placeAddr = placeAddr;
        this.imgUrl = imgUrl;
    }
}
