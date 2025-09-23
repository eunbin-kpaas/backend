package com.localtrip.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 페이징 응답 표준화 클래스
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> {
    
    private final boolean success;
    private final List<T> data;
    private final PageInfo pageInfo;
    private final String message;
    private final LocalDateTime timestamp;
    
    private PageResponse(List<T> data, PageInfo pageInfo, String message) {
        this.success = true;
        this.data = data;
        this.pageInfo = pageInfo;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    private PageResponse(String message) {
        this.success = false;
        this.data = null;
        this.pageInfo = null;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
    
    // 성공 응답 생성
    public static <T> PageResponse<T> success(List<T> data, int currentPage, int pageSize, long totalElements) {
        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElements);
        return new PageResponse<>(data, pageInfo, null);
    }
    
    public static <T> PageResponse<T> success(List<T> data, int currentPage, int pageSize, long totalElements, String message) {
        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElements);
        return new PageResponse<>(data, pageInfo, message);
    }
    
    // 실패 응답 생성
    public static <T> PageResponse<T> fail(String message) {
        return new PageResponse<>(message);
    }
    
    // Getters
    public boolean isSuccess() {
        return success;
    }
    
    public List<T> getData() {
        return data;
    }
    
    public PageInfo getPageInfo() {
        return pageInfo;
    }
    
    public String getMessage() {
        return message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /**
     * 페이징 정보 클래스
     */
    public static class PageInfo {
        private final int currentPage;      // 현재 페이지 (0부터 시작)
        private final int pageSize;         // 페이지 크기
        private final long totalElements;   // 전체 요소 수
        private final int totalPages;       // 전체 페이지 수
        private final boolean hasNext;      // 다음 페이지 존재 여부
        private final boolean hasPrevious;  // 이전 페이지 존재 여부
        private final boolean first;        // 첫 페이지 여부
        private final boolean last;         // 마지막 페이지 여부
        
        public PageInfo(int currentPage, int pageSize, long totalElements) {
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalElements = totalElements;
            this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
            this.hasNext = currentPage + 1 < totalPages;
            this.hasPrevious = currentPage > 0;
            this.first = currentPage == 0;
            this.last = currentPage + 1 >= totalPages;
        }
        
        // Getters
        public int getCurrentPage() { return currentPage; }
        public int getPageSize() { return pageSize; }
        public long getTotalElements() { return totalElements; }
        public int getTotalPages() { return totalPages; }
        public boolean isHasNext() { return hasNext; }
        public boolean isHasPrevious() { return hasPrevious; }
        public boolean isFirst() { return first; }
        public boolean isLast() { return last; }
    }
}
