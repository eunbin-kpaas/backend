package com.localtrip.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 아이디 중복 확인 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckIdResponse {
    
    private boolean available;
    private String message;
    
    public static CheckIdResponse available() {
        return new CheckIdResponse(true, "사용 가능한 아이디입니다.");
    }
    
    public static CheckIdResponse unavailable() {
        return new CheckIdResponse(false, "이미 사용중인 아이디입니다.");
    }
}
