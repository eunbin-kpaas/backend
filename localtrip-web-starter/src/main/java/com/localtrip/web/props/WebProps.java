package com.localtrip.web.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Validated
@ConfigurationProperties(prefix = "localtrip.web")
public class WebProps {
    private boolean requestLoggingEnabled = true;

    @Min(0) @Max(1)
    private double sampleRate = 1.0;

    private boolean logRequestBody = false;
    private int maxBodySizeBytes = 2048;
    private String[] excludePatterns = {"/actuator/**", "/health", "/static/**"};
    
    // 글로벌 로깅 설정
    private boolean globalLoggingEnabled = true;
    
    // Getters and Setters
    public boolean isRequestLoggingEnabled() {
        return requestLoggingEnabled;
    }
    
    public void setRequestLoggingEnabled(boolean requestLoggingEnabled) {
        this.requestLoggingEnabled = requestLoggingEnabled;
    }
    
    public double getSampleRate() {
        return sampleRate;
    }
    
    public void setSampleRate(double sampleRate) {
        this.sampleRate = sampleRate;
    }
    
    public boolean isLogRequestBody() {
        return logRequestBody;
    }
    
    public void setLogRequestBody(boolean logRequestBody) {
        this.logRequestBody = logRequestBody;
    }
    
    public int getMaxBodySizeBytes() {
        return maxBodySizeBytes;
    }
    
    public void setMaxBodySizeBytes(int maxBodySizeBytes) {
        this.maxBodySizeBytes = maxBodySizeBytes;
    }
    
    public String[] getExcludePatterns() {
        return excludePatterns;
    }
    
    public void setExcludePatterns(String[] excludePatterns) {
        this.excludePatterns = excludePatterns;
    }
    
    public boolean isGlobalLoggingEnabled() {
        return globalLoggingEnabled;
    }
    
    public void setGlobalLoggingEnabled(boolean globalLoggingEnabled) {
        this.globalLoggingEnabled = globalLoggingEnabled;
    }
}