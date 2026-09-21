package com.sentinelguard.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class globalExceptionHandlers {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> handleException(MethodArgumentNotValidException exception)
    {
        Map <String,String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error->errors.put(error.getField(),error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
    //usernotfound exception
    @ExceptionHandler(userNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleUserNotFound(userNotFoundException exception)
    {
        Map<String,String> error = new HashMap<>();
        error.put("error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //duplicate exception
    @ExceptionHandler(duplicateUserException.class)
    public ResponseEntity<Map<String,String>> handleDuplicate(duplicateUserException exception)
    {
        Map<String,String> error = new HashMap<>();
        error.put("error", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
