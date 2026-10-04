package com.sentinelguard.auth_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userProfileRequest {

    private String username;
    private String email;
    private String fullName;
}