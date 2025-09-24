package com.localtrip.web.config;

import com.localtrip.web.filter.GlobalLoggingFilter;
import com.localtrip.web.filter.RequestLoggingFilter;
import com.localtrip.web.filter.SecurityHeadersFilter;
import com.localtrip.web.handler.GlobalExceptionHandler;
import com.localtrip.web.props.WebProps;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(WebProps.class)
public class LocalTripWebAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix="localtrip.web", name="request-logging-enabled",
            havingValue="true", matchIfMissing = true)
    @ConditionalOnMissingBean(name = "requestLoggingFilter")
    public RequestLoggingFilter requestLoggingFilter(WebProps props) {
        return new RequestLoggingFilter(props);
    }
    
    @Bean
    @ConditionalOnProperty(prefix="localtrip.web", name="global-logging-enabled",
            havingValue="true", matchIfMissing = true)
    @ConditionalOnMissingBean(name = "globalLoggingFilter")
    public GlobalLoggingFilter globalLoggingFilter(WebProps props) {
        return new GlobalLoggingFilter(props);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
    
    @Bean
    @ConditionalOnProperty(prefix="localtrip.web.security", name="headers-enabled",
            havingValue="true", matchIfMissing = true)
    @ConditionalOnMissingBean(name = "securityHeadersFilter")
    public SecurityHeadersFilter securityHeadersFilter() {
        return new SecurityHeadersFilter();
    }
}