package com.sentinelguard.auth_service.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class refreshTokenRequest {
    @NotBlank
    private String refreshToken;
}
