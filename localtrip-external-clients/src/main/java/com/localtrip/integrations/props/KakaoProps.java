package com.localtrip.integrations.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "localtrip.integrations.kakao")
public class KakaoProps {
    private String baseUrl = "https://dapi.kakao.com";
    private String apiKey;                    // 환경변수로 주입
    private int connectTimeoutMs = 1000;
    private int readTimeoutMs = 3000;
    
    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }
    
    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }
    
    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }
    
    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }
}