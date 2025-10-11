package com.localtrip.member.service;

import com.localtrip.common.exception.custom.BusinessLogicException;
import com.localtrip.common.util.RedisUtil;
import com.localtrip.member.config.MailProperties;
import com.localtrip.member.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

/**
 * 이메일 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final MailProperties mailProperties;
    private final SimpleMailMessage verificationMailTemplate;
    
    private static final String VERIFICATION_CODE_PREFIX = "email_verification:";
    private static final Random random = new Random();
    
    /**
     * 이메일 인증코드 발송
     */
    public void sendVerificationCode(String email) {
        try {
            // 1. 6자리 인증코드 생성
            String verificationCode = generateVerificationCode();
            
            // 2. Redis에 인증코드 저장 (TTL: 5분)
            String redisKey = VERIFICATION_CODE_PREFIX + email;
            Duration expiry = Duration.ofMinutes(mailProperties.getExpiryMinutes());
            redisUtil.setString(redisKey, verificationCode, expiry);
            
            // 3. 이메일 발송
            SimpleMailMessage message = new SimpleMailMessage(verificationMailTemplate);
            message.setTo(email);
            message.setSubject(mailProperties.getSubject());
            message.setText(buildVerificationEmailContent(verificationCode));
            
            mailSender.send(message);
            
            log.info("인증코드 발송 완료: email={}, code={}", email, verificationCode);
            
        } catch (Exception e) {
            log.error("이메일 발송 실패: email={}, error={}", email, e.getMessage(), e);
            throw new BusinessLogicException(MemberErrorCode.EMAIL_SEND_FAILED);
        }
    }
    
    /**
     * 이메일 인증코드 검증
     */
    public boolean verifyCode(String email, String code) {
        String redisKey = VERIFICATION_CODE_PREFIX + email;
        String storedCode = redisUtil.getString(redisKey);
        
        if (storedCode == null) {
            log.warn("인증코드 만료 또는 미존재: email={}", email);
            throw new BusinessLogicException(MemberErrorCode.VERIFICATION_CODE_EXPIRED);
        }
        
        if (!storedCode.equals(code)) {
            log.warn("인증코드 불일치: email={}, input={}, stored={}", email, code, storedCode);
            throw new BusinessLogicException(MemberErrorCode.VERIFICATION_CODE_INVALID);
        }
        
        // 인증 성공 시 인증코드 삭제
        redisUtil.delete(redisKey);
        
        // 인증 완료 표시 (30분 유효)
        String verifiedKey = "email_verified:" + email;
        redisUtil.setString(verifiedKey, "true", Duration.ofMinutes(30));
        
        log.info("이메일 인증 성공: email={}", email);
        return true;
    }
    
    /**
     * 이메일 인증 완료 여부 확인
     */
    public boolean isEmailVerified(String email) {
        String verifiedKey = "email_verified:" + email;
        return redisUtil.exists(verifiedKey);
    }
    
    /**
     * 이메일 인증 완료 표시 삭제 (회원가입 완료 후)
     */
    public void clearEmailVerification(String email) {
        String verifiedKey = "email_verified:" + email;
        redisUtil.delete(verifiedKey);
    }
    
    /**
     * 6자리 인증코드 생성
     */
    private String generateVerificationCode() {
        return String.format("%06d", random.nextInt(1000000));
    }
    
    /**
     * 인증 이메일 내용 생성
     */
    private String buildVerificationEmailContent(String verificationCode) {
        return String.format(
            "안녕하세요, LocalTrip입니다.\n\n" +
            "회원가입을 위한 이메일 인증코드입니다.\n\n" +
            "인증코드: %s\n\n" +
            "이 코드는 %d분 후에 만료됩니다.\n" +
            "인증코드를 입력하여 이메일 인증을 완료해주세요.\n\n" +
            "감사합니다.",
            verificationCode,
            mailProperties.getExpiryMinutes()
        );
    }
}
