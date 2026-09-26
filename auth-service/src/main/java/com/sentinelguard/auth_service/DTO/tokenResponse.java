package com.sentinelguard.auth_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class tokenResponse {
    private String accessToken;
    private String refreshToken;


}
