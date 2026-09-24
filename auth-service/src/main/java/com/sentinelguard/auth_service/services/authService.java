package com.sentinelguard.auth_service.services;

import com.sentinelguard.auth_service.DTO.loginRequest;
import com.sentinelguard.auth_service.DTO.registerRequest;
import com.sentinelguard.auth_service.DTO.registerResponse;
import com.sentinelguard.auth_service.exception.invalidCredentialException;
import com.sentinelguard.auth_service.jwt.jwtService;
import com.sentinelguard.auth_service.model.authUser;
import com.sentinelguard.auth_service.model.role;
import com.sentinelguard.auth_service.repo.authUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class authService {
    private final authUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final jwtService jwtService;

    //registration----------->
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
    //login------------>
    public String login(loginRequest request)
    {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            );
            GrantedAuthority authority = authentication.getAuthorities()
                    .iterator()
                    .next();
            return jwtService.generateAccessToken(
                    authentication.getName(),
                    authority.getAuthority()


            );
        }catch (AuthenticationException e)
        {
            throw new invalidCredentialException("invalid username or password");
        }

    }
}
