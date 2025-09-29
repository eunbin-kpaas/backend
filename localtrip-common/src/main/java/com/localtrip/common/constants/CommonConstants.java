package com.localtrip.common.constants;

/**
 * 공통 상수 정의
 */
public final class CommonConstants {
    
    // 생성자 private로 인스턴스화 방지
    private CommonConstants() {}
    
    // 시간 관련
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int MAX_TRAVEL_DAYS = 14;
    public static final int MAX_GROUP_SIZE = 20;
    public static final int MIN_GROUP_SIZE = 1;
    
    // 문자열 길이 제한
    public static final int MAX_PLAN_NAME_LENGTH = 50;
    public static final int MAX_NICKNAME_LENGTH = 10;
    public static final int MIN_NICKNAME_LENGTH = 2;
    public static final int MAX_MEMO_LENGTH = 500;
    
    // 파일 관련
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String[] ALLOWED_IMAGE_TYPES = {"jpg", "jpeg", "png", "webp"};
    
    // API 관련
    public static final int DEFAULT_TIMEOUT_SECONDS = 10;
    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final String DEFAULT_CHARSET = "UTF-8";
    
    // 비즈니스 규칙
    public static final int RECOMMENDATION_LIMIT = 5; // 추천 장소 개수
    public static final int BASKET_MAX_ITEMS = 100;   // 바구니 최대 아이템
    public static final int RECENT_VIEWED_LIMIT = 20; // 최근 본 장소
    
    // 정규식 패턴
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String PHONE_REGEX = "^010-\\d{4}-\\d{4}$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]+$";
    
    // 캐시 관련
    public static final int CACHE_DEFAULT_TTL = 3600; // 1시간
    public static final int CACHE_SHORT_TTL = 300;    // 5분
    public static final int CACHE_LONG_TTL = 86400;   // 24시간
    
    // HTTP 헤더
    public static final String HEADER_REQUEST_ID = "X-Request-ID";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    
    // 날짜 포맷
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm";
}
