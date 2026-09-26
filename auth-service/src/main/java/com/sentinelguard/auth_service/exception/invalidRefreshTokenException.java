package com.sentinelguard.auth_service.exception;

public class invalidRefreshTokenException extends RuntimeException{
    public invalidRefreshTokenException(String message)
    {
        super(message);
    }
}
