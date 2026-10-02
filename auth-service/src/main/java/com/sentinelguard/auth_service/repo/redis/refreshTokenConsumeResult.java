package com.sentinelguard.auth_service.repo.redis;

public enum refreshTokenConsumeResult {

    SUCCESS,
    REUSED,
    INVALID,
    FAMILY_REVOKED,
    CONFLICT
}
