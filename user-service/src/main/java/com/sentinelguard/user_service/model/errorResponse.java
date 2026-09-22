package com.sentinelguard.user_service.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class errorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    Map<String,String> validationErrors;

}
