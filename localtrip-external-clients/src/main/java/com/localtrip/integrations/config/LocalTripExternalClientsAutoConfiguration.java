package com.localtrip.integrations.config;

import com.localtrip.integrations.props.GooglePlacesProps;
import com.localtrip.integrations.props.KakaoProps;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@AutoConfiguration
@EnableConfigurationProperties({KakaoProps.class, GooglePlacesProps.class})
public class LocalTripExternalClientsAutoConfiguration {

    @Bean("kakaoWebClient")
    @ConditionalOnProperty(prefix = "localtrip.integrations.kakao", name = "api-key")
    @ConditionalOnMissingBean(name = "kakaoWebClient")
    public WebClient kakaoWebClient(KakaoProps kakaoProps) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(kakaoProps.getReadTimeoutMs()));
        
        return WebClient.builder()
                .baseUrl(kakaoProps.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Authorization", "KakaoAK " + kakaoProps.getApiKey())
                .build();
    }

    @Bean("googlePlacesWebClient")
    @ConditionalOnProperty(prefix = "localtrip.integrations.google", name = "api-key")
    @ConditionalOnMissingBean(name = "googlePlacesWebClient")
    public WebClient googlePlacesWebClient(GooglePlacesProps googleProps) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMillis(googleProps.getReadTimeoutMs()));
        
        return WebClient.builder()
                .baseUrl(googleProps.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
