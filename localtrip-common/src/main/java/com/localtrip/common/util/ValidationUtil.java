package com.localtrip.common.util;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 공통 유효성 검증 유틸리티
 */
public class ValidationUtil {
    
    // 정규식 패턴들
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$"
    );
    
    private static final Pattern USER_ID_PATTERN = Pattern.compile(
        "^[A-Za-z0-9]{6,20}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^010-\\d{4}-\\d{4}$"
    );
    
    /**
     * 이메일 유효성 검증
     */
    public static boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 비밀번호 유효성 검증 (영문, 숫자, 특수문자 포함 8-20자)
     */
    public static boolean isValidPassword(String password) {
        return StringUtils.hasText(password) && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * 사용자 ID 유효성 검증 (영문, 숫자 6-20자)
     */
    public static boolean isValidUserId(String userId) {
        return StringUtils.hasText(userId) && USER_ID_PATTERN.matcher(userId).matches();
    }
    
    /**
     * 전화번호 유효성 검증 (010-1234-5678 형식)
     */
    public static boolean isValidPhone(String phone) {
        return StringUtils.hasText(phone) && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * 닉네임 유효성 검증 (2-10자, 한글/영문/숫자)
     */
    public static boolean isValidNickname(String nickname) {
        if (!StringUtils.hasText(nickname)) {
            return false;
        }
        
        int length = nickname.trim().length();
        return length >= 2 && length <= 10 && nickname.matches("^[가-힣A-Za-z0-9]+$");
    }
    
    /**
     * 여행 계획 이름 유효성 검증 (1-50자)
     */
    public static boolean isValidPlanName(String planName) {
        if (!StringUtils.hasText(planName)) {
            return false;
        }
        
        int length = planName.trim().length();
        return length >= 1 && length <= 50;
    }
    
    /**
     * 인원수 유효성 검증 (1-20명)
     */
    public static boolean isValidGroupSize(Integer groupSize) {
        return groupSize != null && groupSize >= 1 && groupSize <= 20;
    }
    
    /**
     * 문자열이 비어있지 않고 길이 제한 내에 있는지 검증
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (!StringUtils.hasText(str)) {
            return false;
        }
        
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * 숫자가 범위 내에 있는지 검증
     */
    public static boolean isInRange(Number number, long min, long max) {
        if (number == null) {
            return false;
        }
        
        long value = number.longValue();
        return value >= min && value <= max;
    }
    
    /**
     * 문자열에 HTML 태그가 포함되어 있는지 검증
     */
    public static boolean containsHtmlTags(String str) {
        if (!StringUtils.hasText(str)) {
            return false;
        }
        
        Pattern htmlPattern = Pattern.compile("<[^>]+>");
        return htmlPattern.matcher(str).find();
    }
}
