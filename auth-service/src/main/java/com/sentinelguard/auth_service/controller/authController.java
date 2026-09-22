package com.sentinelguard.auth_service.controller;

import com.sentinelguard.auth_service.DTO.registerRequest;
import com.sentinelguard.auth_service.DTO.registerResponse;
import com.sentinelguard.auth_service.services.authService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class authController {
    @Autowired
    authService authService;


    @PostMapping("/register")
    public ResponseEntity<registerResponse> saved(@RequestBody registerRequest request)
    {
        return ResponseEntity.ok(authService.register(request));
    }
}
