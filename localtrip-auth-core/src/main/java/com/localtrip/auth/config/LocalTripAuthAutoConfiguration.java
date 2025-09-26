package com.localtrip.auth.config;

import com.localtrip.auth.props.AuthProps;
import com.localtrip.auth.handler.AuthenticationEntryPointImpl;
import com.localtrip.auth.handler.AccessDeniedHandlerImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@EnableConfigurationProperties(AuthProps.class)
@Import({SecurityConfig.class, AuthenticationEntryPointImpl.class, AccessDeniedHandlerImpl.class})
public class LocalTripAuthAutoConfiguration {
    // SecurityConfig에서 필요한 빈들을 자동으로 등록
}
