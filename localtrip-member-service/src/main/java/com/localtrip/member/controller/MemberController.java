package com.localtrip.member.controller;

import com.localtrip.web.dto.BaseResponse;
import com.localtrip.member.dto.request.LoginRequest;
import com.localtrip.member.dto.response.LoginResponse;
import com.localtrip.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    /**
     * 로그인
     */
    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                           HttpServletResponse response) {
        log.debug("로그인 요청: memberId={}", loginRequest.getMemberId());
        
        MemberService.LoginResult loginResult = memberService.login(loginRequest);
        
        // Access Token을 Response Header에 설정
        response.setHeader("Authorization", "Bearer " + loginResult.getAccessToken());
        
        // Refresh Token을 HTTP Only Cookie에 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", loginResult.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // HTTPS에서는 true로 설정
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(refreshTokenCookie);
        
        return BaseResponse.success(loginResult.getResponse());
    }
}
