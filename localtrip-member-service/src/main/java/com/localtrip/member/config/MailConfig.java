package com.localtrip.member.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 메일 설정
 */
@Configuration
@EnableAsync
public class MailConfig {
    
    /**
     * 이메일 인증 템플릿
     */
    @Bean
    public SimpleMailMessage verificationMailTemplate() {
        SimpleMailMessage template = new SimpleMailMessage();
        template.setFrom("choeunbin0324@gmail.com");
        template.setSubject("[LocalTrip] 이메일 인증 코드");
        return template;
    }
}
