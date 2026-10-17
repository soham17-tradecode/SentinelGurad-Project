package com.sentinelguard.auth_service.services;

import com.sentinelguard.auth_service.model.authUser;
import com.sentinelguard.auth_service.model.role;
import com.sentinelguard.auth_service.repo.authUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class initialAdminSetupService {

    private final JdbcTemplate jdbcTemplate;
    private final authUserRepo authUserRepo;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void createInitialAdmin(
            String username,
            String email,
            String rawPassword) {

        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || rawPassword == null || rawPassword.length() < 12) {
            throw new IllegalArgumentException(
                    "Username and email are required; password must contain at least 12 characters");
        }

        // Serialize all initial-admin setup attempts.
        jdbcTemplate.execute("SELECT pg_advisory_xact_lock(782341)");

        if (authUserRepo.existsByRole(role.ADMIN)) {
            throw new IllegalStateException(
                    "Initial administrator setup is already completed");
        }

        if (authUserRepo.existsByUsername(username)
                || authUserRepo.existsByEmail(email)) {
            throw new IllegalStateException(
                    "Username or email already exists");
        }

        authUser admin = authUser.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(role.ADMIN)
                .build();

        authUserRepo.saveAndFlush(admin);
    }
}
