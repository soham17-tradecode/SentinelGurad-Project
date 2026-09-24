package com.sentinelguard.auth_service.controller;

import com.sentinelguard.auth_service.DTO.*;
import com.sentinelguard.auth_service.model.authUser;
import com.sentinelguard.auth_service.repo.authUserRepo;
import com.sentinelguard.auth_service.services.authService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<String> login(@Valid @RequestBody loginRequest request) {

        String accessToken = authService.login(request);


        return ResponseEntity.ok(accessToken);


    }
}
