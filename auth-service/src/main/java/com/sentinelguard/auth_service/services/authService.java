package com.sentinelguard.auth_service.services;

import com.sentinelguard.auth_service.DTO.registerRequest;
import com.sentinelguard.auth_service.DTO.registerResponse;
import com.sentinelguard.auth_service.model.authUser;
import com.sentinelguard.auth_service.model.role;
import com.sentinelguard.auth_service.repo.authUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class authService {
    private final authUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public registerResponse register(registerRequest registerRequest)
    {
        if (userRepo.existsByUsername(registerRequest.getUsername()))
        {
            throw new RuntimeException("username already exists");
        }
        if (userRepo.existsByEmail(registerRequest.getEmail()))
        {
            throw new RuntimeException("email already exists");
        }
        authUser user = authUser.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(role.USER)
                .build();
         userRepo.save(user);
         return registerResponse.builder()
                 .username(user.getUsername())
                 .email(user.getEmail())
                 .role(user.getRole().name())
                 .build();
    }
}
