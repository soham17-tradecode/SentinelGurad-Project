package com.sentinelguard.user_service.exception;

public class userNotFoundException extends RuntimeException{

    public userNotFoundException(String message)
    {
        super(message);
    }


}
