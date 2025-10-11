package com.localtrip.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 여행바구니 (사용자-지역 연결)
 */
@Entity
@Table(name = "travel_basket")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelBasket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    
    // 생성자
    public TravelBasket(Long memberId) {
        this.memberId = memberId;
    }
}
