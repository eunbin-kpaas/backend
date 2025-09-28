package com.localtrip.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 회원가입 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponse {
    
    private String memberId;
    private String email;
    private int memberType;
    private LocalDateTime memberCreatedAt;
    
    public static SignupResponse from(String memberId, String email, int memberType, LocalDateTime createdAt) {
        return new SignupResponse(memberId, email, memberType, createdAt);
    }
}
