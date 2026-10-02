package com.sentinelguard.auth_service.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class registerResponse {
    private String username;
    private String email;
    private String role;
}
