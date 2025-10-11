package com.localtrip.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

/**
 * 날짜/시간 처리 유틸리티 (여행 플랫폼 특화)
 */
public class DateTimeUtil {
    
    // 한국 시간대
    public static final ZoneId KST = ZoneId.of("Asia/Seoul");
    
    // 공통 포맷터들
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter COMPACT_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    
    /**
     * 현재 한국 시간 반환
     */
    public static LocalDateTime nowKST() {
        return LocalDateTime.now(KST);
    }
    
    /**
     * 현재 한국 날짜 반환
     */
    public static LocalDate todayKST() {
        return LocalDate.now(KST);
    }
    
    /**
     * 여행 기간 계산 (일 단위)
     */
    public static long getTravelDays(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 for inclusive
    }
    
    /**
     * 여행 날짜 목록 생성
     */
    public static List<LocalDate> getTravelDateList(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .toList();
    }
    
    /**
     * 여행 날짜 유효성 검증
     */
    public static boolean isValidTravelPeriod(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        
        LocalDate today = todayKST();
        
        // 시작일은 오늘 이후여야 함
        if (startDate.isBefore(today)) {
            return false;
        }
        
        // 종료일은 시작일과 같거나 이후여야 함
        if (endDate.isBefore(startDate)) {
            return false;
        }
        
        // 최대 14일까지만 허용 (요구사항 참조)
        long days = getTravelDays(startDate, endDate);
        return days <= 14;
    }
    
    /**
     * 문자열을 LocalDate로 변환
     */
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 문자열을 LocalDateTime으로 변환
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, DATETIME_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * LocalDate를 문자열로 변환
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMAT) : null;
    }
    
    /**
     * LocalDateTime을 문자열로 변환
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMAT) : null;
    }
    
    /**
     * 캐시 키용 날짜 문자열 생성 (yyyyMMdd)
     */
    public static String toCacheKeyDate(LocalDate date) {
        return date != null ? date.format(COMPACT_FORMAT) : null;
    }
    
    /**
     * 여행 계획 만료 시간 계산 (여행 종료일 + 30일)
     */
    public static LocalDateTime calculatePlanExpiration(LocalDate travelEndDate) {
        return travelEndDate.plusDays(30).atStartOfDay();
    }
}
