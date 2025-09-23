package com.localtrip.common.constants;

/**
 * 캐시 키 관련 상수
 */
public final class CacheConstants {
    
    // 생성자 private로 인스턴스화 방지
    private CacheConstants() {}
    
    // 캐시 키 프리픽스
    public static final String CACHE_PREFIX = "localtrip:";
    
    // 회원 관련 캐시
    public static final String MEMBER_PREFIX = CACHE_PREFIX + "member:";
    public static final String EMAIL_VERIFICATION_PREFIX = MEMBER_PREFIX + "email_verification:";
    public static final String PASSWORD_RESET_PREFIX = MEMBER_PREFIX + "password_reset:";
    public static final String MEMBER_INFO_PREFIX = MEMBER_PREFIX + "info:";
    
    // 인증 관련 캐시  
    public static final String AUTH_PREFIX = CACHE_PREFIX + "auth:";
    public static final String JWT_BLACKLIST_PREFIX = AUTH_PREFIX + "blacklist:";
    public static final String LOGIN_ATTEMPT_PREFIX = AUTH_PREFIX + "login_attempt:";
    
    // 장소 관련 캐시
    public static final String PLACE_PREFIX = CACHE_PREFIX + "place:";
    public static final String PLACE_INFO_PREFIX = PLACE_PREFIX + "info:";
    public static final String PLACE_LIST_PREFIX = PLACE_PREFIX + "list:";
    public static final String PLACE_SEARCH_PREFIX = PLACE_PREFIX + "search:";
    
    // 지역 관련 캐시
    public static final String REGION_PREFIX = CACHE_PREFIX + "region:";
    public static final String REGION_INFO_PREFIX = REGION_PREFIX + "info:";
    public static final String POPULAR_PLACES_PREFIX = REGION_PREFIX + "popular:";
    public static final String LOCAL_SPOTS_PREFIX = REGION_PREFIX + "local_spots:";
    
    // 여행계획 관련 캐시
    public static final String PLAN_PREFIX = CACHE_PREFIX + "plan:";
    public static final String PLAN_INFO_PREFIX = PLAN_PREFIX + "info:";
    public static final String PLAN_LIST_PREFIX = PLAN_PREFIX + "list:";
    
    // 바구니 관련 캐시
    public static final String BASKET_PREFIX = CACHE_PREFIX + "basket:";
    public static final String USER_BASKET_PREFIX = BASKET_PREFIX + "user:";
    
    // 외부 API 관련 캐시
    public static final String EXTERNAL_PREFIX = CACHE_PREFIX + "external:";
    public static final String KAKAO_API_PREFIX = EXTERNAL_PREFIX + "kakao:";
    public static final String GOOGLE_API_PREFIX = EXTERNAL_PREFIX + "google:";
    
    // 통계 관련 캐시
    public static final String STATS_PREFIX = CACHE_PREFIX + "stats:";
    public static final String DAILY_STATS_PREFIX = STATS_PREFIX + "daily:";
    public static final String API_USAGE_PREFIX = STATS_PREFIX + "api_usage:";
    
    // 캐시 TTL (초 단위)
    public static final long EMAIL_VERIFICATION_TTL = 300; // 5분
    public static final long PASSWORD_RESET_TTL = 1800;    // 30분
    public static final long PLACE_INFO_TTL = 3600;        // 1시간
    public static final long PLACE_LIST_TTL = 1800;        // 30분
    public static final long POPULAR_PLACES_TTL = 3600;    // 1시간
    public static final long SEARCH_RESULT_TTL = 600;      // 10분
    public static final long USER_SESSION_TTL = 7200;      // 2시간
    public static final long API_RATE_LIMIT_TTL = 60;      // 1분
    
    // 캐시 키 생성 헬퍼 메서드들
    public static String memberInfo(String memberId) {
        return MEMBER_INFO_PREFIX + memberId;
    }
    
    public static String emailVerification(String email) {
        return EMAIL_VERIFICATION_PREFIX + email;
    }
    
    public static String placeInfo(String placeId) {
        return PLACE_INFO_PREFIX + placeId;
    }
    
    public static String placeList(String regionCode, String category) {
        return PLACE_LIST_PREFIX + regionCode + ":" + category;
    }
    
    public static String popularPlaces(String regionCode) {
        return POPULAR_PLACES_PREFIX + regionCode;
    }
    
    public static String userBasket(String memberId) {
        return USER_BASKET_PREFIX + memberId;
    }
    
    public static String planInfo(String planId) {
        return PLAN_INFO_PREFIX + planId;
    }
    
    public static String jwtBlacklist(String tokenId) {
        return JWT_BLACKLIST_PREFIX + tokenId;
    }
    
    public static String loginAttempt(String identifier) {
        return LOGIN_ATTEMPT_PREFIX + identifier;
    }
}
