package com.sentinelguard.user_service.exception;

public class duplicateUserException extends RuntimeException{

    public duplicateUserException(String message)
    {
        super(message);
    }
}
