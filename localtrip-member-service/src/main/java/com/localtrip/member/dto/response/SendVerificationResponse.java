package com.localtrip.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이메일 인증코드 발송 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SendVerificationResponse {
    
    private String message;
    private int expiryMinutes;
    
    public static SendVerificationResponse success(int expiryMinutes) {
        return new SendVerificationResponse("인증코드가 발송되었습니다.", expiryMinutes);
    }
}
