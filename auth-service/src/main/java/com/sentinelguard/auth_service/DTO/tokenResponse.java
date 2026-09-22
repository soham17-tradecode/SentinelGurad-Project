package com.sentinelguard.auth_service.DTO;

import lombok.Data;

@Data
public class tokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
}
