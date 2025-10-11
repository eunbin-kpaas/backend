package com.localtrip.member.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 메일 설정 프로퍼티
 */
@Configuration
@ConfigurationProperties(prefix = "mail.verification")
@Getter
@Setter
public class MailProperties {
    
    private String subject;
    private int expiryMinutes;
}
