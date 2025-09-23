package com.localtrip.common.exception;

public interface ErrorCode {
    
    int getStatus();
    String getCode();  
    String getMessage();
    String getService();
}
