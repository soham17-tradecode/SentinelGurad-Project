package com.sentinelguard.auth_service.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class loginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
