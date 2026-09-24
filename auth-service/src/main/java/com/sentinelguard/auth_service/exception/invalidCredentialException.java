package com.sentinelguard.auth_service.exception;

public class invalidCredentialException extends RuntimeException{

    public invalidCredentialException(String message)
    {
        super(message);
    }
}
