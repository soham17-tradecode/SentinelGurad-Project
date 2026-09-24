package com.sentinelguard.auth_service.DTO;

import lombok.Data;

@Data
public class errorResponse {
    private String message;
    private String path;
    private String error;
    private int status;

}
