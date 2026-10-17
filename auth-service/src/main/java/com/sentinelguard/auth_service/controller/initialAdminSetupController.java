package com.sentinelguard.auth_service.controller;

import com.sentinelguard.auth_service.services.initialAdminSetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

@RestController
@RequestMapping("/auth/setup")
@RequiredArgsConstructor
public class initialAdminSetupController {

    @Value("${app.bootstrap-admin.secret}")
    private String setupSecret;

    private final initialAdminSetupService initialAdminSetupService;

    @PostMapping("/initial-admin")
    public ResponseEntity<Map<String, String>> createInitialAdmin(
            @RequestHeader(value = "X-Initial-Setup-Secret", required = false)
            String providedSecret,
            @RequestBody InitialAdminRequest request) {

        try {
            if (providedSecret == null
                    || !MessageDigest.isEqual(
                    setupSecret.getBytes(StandardCharsets.UTF_8),
                    providedSecret.getBytes(StandardCharsets.UTF_8))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Invalid setup authorization"));
            }


            initialAdminSetupService.createInitialAdmin(
                    request.username(),
                    request.email(),
                    request.password());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message",
                            "Initial administrator created successfully"));

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", exception.getMessage()));

        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", exception.getMessage()));
        }
    }

    public record InitialAdminRequest(
            String username,
            String email,
            String password) {
    }

}
