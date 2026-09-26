package com.sentinelguard.auth_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class refreshTokenRotationResponse {
    private String username;
    private String refreshToken;
}
