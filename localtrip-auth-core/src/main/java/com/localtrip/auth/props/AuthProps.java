package com.localtrip.auth.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "localtrip.auth")
public class AuthProps {
    /** 예: https://auth.example.com/.well-known/jwks.json */
    private String jwkSetUri = "http://localhost:8080/.well-known/jwks.json";
    
    public String getJwkSetUri() {
        return jwkSetUri;
    }
    
    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }
}