package com.sentinelguard.auth_service.repo.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class refreshTokenConsumeResponse {

    private refreshTokenConsumeResult result;
    private String username;
    private String familyId;
}
