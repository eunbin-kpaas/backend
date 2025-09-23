package com.localtrip.web.config;

import com.localtrip.web.props.WebProps;
import com.localtrip.web.filter.RequestLoggingFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(WebProps.class)  // ← 중요
public class LocalTripWebAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix="localtrip.web", name="request-logging-enabled",
            havingValue="true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public RequestLoggingFilter requestLoggingFilter(WebProps props) {
        return new RequestLoggingFilter(props);
    }
}