package com.localtrip.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 로그인 응답 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    private MemberInfo memberInfo;
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfo {
        private String memberId;
        private String email;
        private int memberType;
        private LocalDateTime memberCreatedAt;
    }
}
