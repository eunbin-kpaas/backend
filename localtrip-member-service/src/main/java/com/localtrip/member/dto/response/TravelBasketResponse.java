package com.localtrip.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 여행바구니 조회 응답
 */
@Getter
@Builder
public class TravelBasketResponse {
    
    private List<RegionBasketGroup> items;
    
    /**
     * 지역별로 그룹화된 여행바구니 아이템
     */
    @Getter
    @Builder
    public static class RegionBasketGroup {
        private String region;  // 여행 지역
        private List<BasketItemInfo> places;  // 해당 지역의 장소 목록
    }
    
    /**
     * 여행바구니 아이템 상세 정보
     */
    @Getter
    @Builder
    public static class BasketItemInfo {
        private Long itemId;  // 아이템 ID (삭제 시 사용)
        private String placeName;  // 장소 이름
        private String placeAddr;  // 장소 주소
        private String imgUrl;  // 이미지 URL
        private String createdAt;  // 추가 날짜
    }
}
