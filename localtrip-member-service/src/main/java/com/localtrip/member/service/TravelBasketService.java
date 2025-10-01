package com.localtrip.member.service;

import com.localtrip.common.exception.GlobalErrorCode;
import com.localtrip.common.exception.custom.BusinessLogicException;
import com.localtrip.member.dto.request.AddBasketItemRequest;
import com.localtrip.member.dto.response.TravelBasketResponse;
import com.localtrip.member.entity.TravelBasket;
import com.localtrip.member.entity.TravelBasketItem;
import com.localtrip.member.repository.TravelBasketItemRepository;
import com.localtrip.member.repository.TravelBasketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelBasketService {
    
    private final TravelBasketRepository basketRepository;
    private final TravelBasketItemRepository basketItemRepository;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * 여행바구니 아이템 조회
     * @param memberId 회원 ID
     * @return 지역별로 그룹화된 여행바구니 아이템 목록
     */
    public TravelBasketResponse getBasketItems(Long memberId) {
        // 1. 회원의 여행바구니 조회 (없으려 빈 목록 반환)
        TravelBasket basket = basketRepository.findByMemberId(memberId).orElse(null);
        
        // 바구니가 없으면 빈 응답 반환
        if (basket == null) {
            return TravelBasketResponse.builder()
                    .items(Collections.emptyList())
                    .build();
        }
        
        // 2. 바구니 아이템 조회 (지역별, 최신순)
        List<TravelBasketItem> items = basketItemRepository
                .findByBasketIdOrderByRegionAndCreatedAt(basket.getId());
        
        // 3. 지역별로 그룹화
        Map<String, List<TravelBasketItem>> groupedByRegion = items.stream()
                .collect(Collectors.groupingBy(
                        TravelBasketItem::getRegion,
                        LinkedHashMap::new,  // 순서 유지
                        Collectors.toList()
                ));
        
        // 4. 응답 DTO 변환
        List<TravelBasketResponse.RegionBasketGroup> regionGroups = groupedByRegion.entrySet().stream()
                .map(entry -> TravelBasketResponse.RegionBasketGroup.builder()
                        .region(entry.getKey())
                        .places(entry.getValue().stream()
                                .map(this::toBasketItemInfo)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
        
        return TravelBasketResponse.builder()
                .items(regionGroups)
                .build();
    }
    
    /**
     * 여행바구니에 아이템 추가
     * @param memberId 회원 ID
     * @param request 추가할 아이템 정보
     */
    @Transactional
    public void addBasketItem(Long memberId, AddBasketItemRequest request) {
        // 1. 회원의 여행바구니 조회 또는 생성
        TravelBasket basket = basketRepository.findByMemberId(memberId)
                .orElseGet(() -> createBasketForMember(memberId));
        
        // 2. 아이템 추가
        TravelBasketItem item = TravelBasketItem.builder()
                .basketId(basket.getId())
                .region(request.getRegion())
                .placeName(request.getPlaceName())
                .placeAddr(request.getPlaceAddr())
                .imgUrl(request.getImgUrl())
                .build();
        
        basketItemRepository.save(item);
        log.info("여행바구니 아이템 추가: memberId={}, region={}, place={}", 
                memberId, request.getRegion(), request.getPlaceName());
    }
    
    /**
     * 여행바구니에서 아이템 삭제
     * @param memberId 회원 ID
     * @param itemId 삭제할 아이템 ID
     */
    @Transactional
    public void deleteBasketItem(Long memberId, Long itemId) {
        // 1. 회원의 여행바구니 조회
        TravelBasket basket = basketRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessLogicException(GlobalErrorCode.RESOURCE_NOT_FOUND));
        
        // 2. 아이템 삭제
        basketItemRepository.deleteByBasketIdAndId(basket.getId(), itemId);
        log.info("여행바구니 아이템 삭제: memberId={}, itemId={}", memberId, itemId);
    }
    
    /**
     * 회원의 여행바구니 생성
     */
    @Transactional
    private TravelBasket createBasketForMember(Long memberId) {
        TravelBasket basket = new TravelBasket(memberId);
        return basketRepository.save(basket);
    }
    
    /**
     * Entity를 DTO로 변환
     */
    private TravelBasketResponse.BasketItemInfo toBasketItemInfo(TravelBasketItem item) {
        return TravelBasketResponse.BasketItemInfo.builder()
                .itemId(item.getId())
                .placeName(item.getPlaceName())
                .placeAddr(item.getPlaceAddr())
                .imgUrl(item.getImgUrl())
                .createdAt(item.getCreatedAt().format(DATE_FORMATTER))
                .build();
    }
}
