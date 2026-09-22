package com.sentinelguard.user_service.exception;

import com.sentinelguard.user_service.model.errorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class globalExceptionHandlers {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<errorResponse> handleException(MethodArgumentNotValidException exception,HttpServletRequest request)
    {
        Map <String,String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error->errors.put(error.getField(),error.getDefaultMessage()));

        errorResponse response = new errorResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.setMessage("Validation failed");
        response.setPath(request.getRequestURI());
        response.setValidationErrors(errors);
        return ResponseEntity.badRequest().body(response);
    }
    //usernotfound exception
    @ExceptionHandler(userNotFoundException.class)
    public ResponseEntity<errorResponse> handleUserNotFound(userNotFoundException exception, HttpServletRequest request)
    {
        errorResponse response = new errorResponse();
//        Map<String,String> error = new HashMap<>();
//        error.put("error", exception.getMessage());
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        response.setMessage(exception.getMessage());
        response.setPath(request.getRequestURI());


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //duplicate exception
    @ExceptionHandler(duplicateUserException.class)
    public ResponseEntity<errorResponse> handleDuplicate(duplicateUserException exception,HttpServletRequest request)
    {
//        Map<String,String> error = new HashMap<>();
//        error.put("error", exception.getMessage());

        errorResponse response = new errorResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setError(HttpStatus.CONFLICT.getReasonPhrase());
        response.setMessage(exception.getMessage());
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<errorResponse> handleDataIntegrityViolation(DataIntegrityViolationException exception,HttpServletRequest request)
    {
        errorResponse response = new errorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setError(HttpStatus.CONFLICT.getReasonPhrase());
        response.setMessage("Username or email already exists");
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

}
