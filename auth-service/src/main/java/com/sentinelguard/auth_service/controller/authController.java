package com.sentinelguard.auth_service.controller;

import com.sentinelguard.auth_service.DTO.*;

import com.sentinelguard.auth_service.services.authService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
public class authController {
    @Autowired
    authService authService;



    @PostMapping("/register")
    public ResponseEntity<registerResponse> saved(@RequestBody registerRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<tokenResponse> login(@Valid @RequestBody loginRequest request) {

        tokenResponse accessToken = authService.login(request);


        return ResponseEntity.ok(accessToken);


    }

    @PostMapping("/refresh")
    public ResponseEntity<tokenResponse> refreshToken(@Valid @RequestBody refreshTokenRequest request)
    {
        return ResponseEntity.ok(authService.refresh(request));
    }
}
