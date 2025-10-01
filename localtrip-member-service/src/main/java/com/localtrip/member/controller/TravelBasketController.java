package com.localtrip.member.controller;

import com.localtrip.auth.model.AuthenticatedUser;
import com.localtrip.member.dto.request.AddBasketItemRequest;
import com.localtrip.member.dto.response.TravelBasketResponse;
import com.localtrip.member.service.TravelBasketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 여행바구니 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/travel-basket")
@RequiredArgsConstructor
public class TravelBasketController {
    
    private final TravelBasketService basketService;
    
    /**
     * 여행바구니 아이템 조회
     * @param user 인증된 사용자
     * @return 지역별로 그룹화된 여행바구니 아이템 목록
     */
    @GetMapping
    public ResponseEntity<TravelBasketResponse> getBasketItems(
            @AuthenticationPrincipal AuthenticatedUser user) {
        
        log.info("여행바구니 조회 요청: memberId={}", user.getId());
        TravelBasketResponse response = basketService.getBasketItems(user.getId());
        return ResponseEntity.ok(response);
    }
    
    /**
     * 여행바구니에 아이템 추가
     * @param user 인증된 사용자
     * @param request 추가할 아이템 정보
     * @return 성공 응답
     */
    @PostMapping
    public ResponseEntity<Void> addBasketItem(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody AddBasketItemRequest request) {
        
        log.info("여행바구니 아이템 추가 요청: memberId={}, region={}, place={}", 
                user.getId(), request.getRegion(), request.getPlaceName());
        
        basketService.addBasketItem(user.getId(), request);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 여행바구니에서 아이템 삭제
     * @param user 인증된 사용자
     * @param itemId 삭제할 아이템 ID
     * @return 성공 응답
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteBasketItem(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable Long itemId) {
        
        log.info("여행바구니 아이템 삭제 요청: memberId={}, itemId={}", user.getId(), itemId);
        basketService.deleteBasketItem(user.getId(), itemId);
        return ResponseEntity.ok().build();
    }
}
