package com.sentinelguard.auth_service.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;


import java.time.Instant;

@Service
@RequiredArgsConstructor
public class jwtService {
    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(String username, String role)
    {
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(username)
                .claim("role",role)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(900))
                .issuer("sentinel-guard-auth")
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet))
                .getTokenValue();
    }

}
