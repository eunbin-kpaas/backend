package com.localtrip.auth.config;

import com.localtrip.auth.props.AuthProps;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@AutoConfiguration
@EnableConfigurationProperties(AuthProps.class)
public class LocalTripAuthAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "localtrip.auth", name = "jwk-set-uri")
    public JwtDecoder jwtDecoder(AuthProps authProps) {
        return NimbusJwtDecoder
            .withJwkSetUri(authProps.getJwkSetUri())
            .build();
    }
}
