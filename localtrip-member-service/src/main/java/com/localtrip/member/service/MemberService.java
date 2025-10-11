package com.localtrip.member.service;

import com.localtrip.common.exception.custom.BusinessLogicException;
import com.localtrip.member.config.JwtUtil;
import com.localtrip.member.dto.request.LoginRequest;
import com.localtrip.member.dto.request.SendVerificationRequest;
import com.localtrip.member.dto.request.SignupRequest;
import com.localtrip.member.dto.request.VerifyEmailRequest;
import com.localtrip.member.dto.response.CheckIdResponse;
import com.localtrip.member.dto.response.LoginResponse;
import com.localtrip.member.dto.response.SendVerificationResponse;
import com.localtrip.member.dto.response.SignupResponse;
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
    private final EmailService emailService;
    
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
     * 아이디 중복 확인
     */
    public CheckIdResponse checkIdDuplicated(String memberId) {
        boolean isDuplicated = memberRepository.existsByMemberId(memberId);
        
        if (isDuplicated) {
            log.debug("아이디 중복: memberId={}", memberId);
            return CheckIdResponse.unavailable();
        } else {
            log.debug("아이디 사용 가능: memberId={}", memberId);
            return CheckIdResponse.available();
        }
    }
    
    /**
     * 이메일 인증코드 발송
     */
    public SendVerificationResponse sendVerificationCode(SendVerificationRequest request) {
        String email = request.getEmail();
        
        // 이메일 중복 확인
        if (memberRepository.existsByEmail(email)) {
            log.warn("이메일 중복: email={}", email);
            throw new BusinessLogicException(MemberErrorCode.EMAIL_DUPLICATED);
        }
        
        // 인증코드 발송
        emailService.sendVerificationCode(email);
        
        log.info("인증코드 발송 요청: email={}", email);
        return SendVerificationResponse.success(5); // 5분
    }
    
    /**
     * 이메일 인증코드 확인
     */
    public void verifyEmail(VerifyEmailRequest request) {
        emailService.verifyCode(request.getEmail(), request.getVerificationCode());
        log.info("이메일 인증 성공: email={}", request.getEmail());
    }
    
    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {
        log.debug("회원가입 시도: memberId={}, email={}", 
                signupRequest.getMemberId(), signupRequest.getEmail());
        
        // 1. 아이디 중복 확인
        if (memberRepository.existsByMemberId(signupRequest.getMemberId())) {
            throw new BusinessLogicException(MemberErrorCode.MEMBER_ID_DUPLICATED);
        }
        
        // 2. 이메일 중복 확인
        if (memberRepository.existsByEmail(signupRequest.getEmail())) {
            throw new BusinessLogicException(MemberErrorCode.EMAIL_DUPLICATED);
        }
        
        // 3. 이메일 인증 완료 여부 확인
        if (!emailService.isEmailVerified(signupRequest.getEmail())) {
            log.warn("이메일 인증 미완료: email={}", signupRequest.getEmail());
            throw new BusinessLogicException(MemberErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        
        // 4. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        
        // 5. 회원 생성
        Member member = Member.builder()
                .memberId(signupRequest.getMemberId())
                .password(encodedPassword)
                .email(signupRequest.getEmail())
                .memberType(1) // 일반회원
                .build();
        
        Member savedMember = memberRepository.save(member);
        
        // 6. 이메일 인증 완료 상태 삭제
        emailService.clearEmailVerification(signupRequest.getEmail());
        
        log.info("회원가입 성공: memberId={}, email={}", 
                savedMember.getMemberId(), savedMember.getEmail());
        
        return SignupResponse.from(
                savedMember.getMemberId(),
                savedMember.getEmail(),
                savedMember.getMemberType(),
                savedMember.getMemberCreatedAt()
        );
    }
    
    /**
     * 회원 조회 (ID로)
     */
    public Member findMemberById(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessLogicException(MemberErrorCode.MEMBER_NOT_FOUND));
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
