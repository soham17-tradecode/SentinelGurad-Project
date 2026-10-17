package com.sentinelGuard.threat_monitoring_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class jwtConfig {

    @Value("${JWT_PUBLIC_KEY_PATH}")
    private String publicKeyPath;

    @Bean
    public JwtDecoder jwtDecoder()throws Exception
    {
        String key = Files.readString(Path.of(publicKeyPath));

        key = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decodedKey = Base64.getDecoder().decode(key);


        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        return NimbusJwtDecoder.withPublicKey(publicKey).build();


    }
}
