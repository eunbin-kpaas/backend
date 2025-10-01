package com.localtrip.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 여행바구니에 아이템 추가 요청
 */
@Getter
@NoArgsConstructor
public class AddBasketItemRequest {
    
    @NotBlank(message = "지역을 입력해주세요")
    private String region;
    
    @NotBlank(message = "장소 이름을 입력해주세요")
    private String placeName;
    
    private String placeAddr;  // 선택사항
    
    private String imgUrl;  // 선택사항
}
