package com.sentinel.sentinel_guard_web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class jwtConfig {
    @Bean
    public JwtDecoder jwtDecoder(
            @Value("classpath:keys/public_key.pem") Resource publicKeyResource)
            throws IOException {

        RSAPublicKey publicKey;

        try (var inputStream = publicKeyResource.getInputStream()) {
            publicKey = RsaKeyConverters.x509().convert(inputStream);
        }

        NimbusJwtDecoder decoder =
                NimbusJwtDecoder.withPublicKey(publicKey).build();

        OAuth2TokenValidator<Jwt> validator =
                new DelegatingOAuth2TokenValidator<>(
                        JwtValidators.createDefaultWithIssuer(
                                "sentinel-guard-auth"));

        decoder.setJwtValidator(validator);

        return decoder;
    }

}
