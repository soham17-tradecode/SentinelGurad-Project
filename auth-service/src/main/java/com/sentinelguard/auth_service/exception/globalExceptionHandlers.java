package com.sentinelguard.auth_service.exception;

import com.sentinelguard.auth_service.DTO.errorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class globalExceptionHandlers {

    //wrong credential exception
    @ExceptionHandler(invalidCredentialException.class)
    public ResponseEntity<errorResponse> invalidUserCredential(invalidCredentialException e, HttpServletRequest request) {
        errorResponse response = new errorResponse();

        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(invalidRefreshTokenException.class)
    public ResponseEntity<errorResponse> InvalidRefreshToken(invalidRefreshTokenException exception,HttpServletRequest request)
    {
        errorResponse response = new errorResponse();
        response.setMessage(exception.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        response.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
