package com.localtrip.member.service;

import com.localtrip.common.exception.custom.BusinessLogicException;
import com.localtrip.member.config.JwtUtil;
import com.localtrip.member.dto.request.LoginRequest;
import com.localtrip.member.dto.response.LoginResponse;
import com.localtrip.member.entity.Member;
import com.localtrip.member.exception.MemberErrorCode;
import com.localtrip.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    /**
     * 로그인 및 토큰 생성
     */
    @Transactional
    public LoginResult login(LoginRequest loginRequest) {
        log.debug("로그인 시도: memberId={}", loginRequest.getMemberId());
        
        // 1. 회원 조회
        Member member = memberRepository.findByMemberId(loginRequest.getMemberId())
                .orElseThrow(() -> new BusinessLogicException(MemberErrorCode.INVALID_LOGIN_CREDENTIALS));
        
        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            log.warn("비밀번호 불일치: memberId={}", loginRequest.getMemberId());
            throw new BusinessLogicException(MemberErrorCode.INVALID_LOGIN_CREDENTIALS);
        }
        
        // 3. JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(
                member.getMemberId(), 
                member.getEmail(), 
                member.getMemberType()
        );
        String refreshToken = jwtUtil.generateRefreshToken(member.getMemberId());
        
        // 4. 응답 생성
        LoginResponse.MemberInfo memberInfo = new LoginResponse.MemberInfo(
                member.getMemberId(),
                member.getEmail(),
                member.getMemberType(),
                member.getMemberCreatedAt()
        );
        
        LoginResponse response = new LoginResponse(memberInfo);
        
        log.info("로그인 성공: memberId={}", member.getMemberId());
        return new LoginResult(accessToken, refreshToken, response);
    }
    
    /**
     * 회원 조회 (ID로)
     */
    public Member findMemberById(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessLogicException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
    
    /**
     * 아이디 중복 확인
     */
    public boolean isIdDuplicated(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }
    
    /**
     * 이메일 중복 확인
     */
    public boolean isEmailDuplicated(String email) {
        return memberRepository.existsByEmail(email);
    }
    
    /**
     * 로그인 결과 래퍼 클래스
     */
    @lombok.Getter
    @lombok.AllArgsConstructor
    public static class LoginResult {
        private final String accessToken;
        private final String refreshToken;
        private final LoginResponse response;
    }
}
