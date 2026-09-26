package com.sentinelguard.auth_service.services;

import com.sentinelguard.auth_service.exception.invalidRefreshTokenException;
import com.sentinelguard.auth_service.repo.redis.refreshTokenRedisRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class refreshTokenService {

    private final SecureRandom secureRandom = new SecureRandom();
    private final refreshTokenRedisRepo refreshTokenRedisRepo;


    public String generateRefreshToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

    }

    public String hashRefreshToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes());

            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');

                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new IllegalStateException("could not hash refresh token");
        }
    }

    public String createRefreshToken(String username, String existingFamilyId) {
        String refreshToken = generateRefreshToken();

        String tokenHash = hashRefreshToken(refreshToken);


        String familyId = existingFamilyId;


        if (familyId == null) {
            familyId = UUID.randomUUID().toString();
            refreshTokenRedisRepo.familyIdStatus(familyId);


        }
        String redisKey = "refresh:" + tokenHash;
        refreshTokenRedisRepo.save(redisKey, username,
                familyId, Duration.ofDays(7));


        return refreshToken;
    }


    public String validateAndGetUsername(String refreshToken) {
        String tokenHash = hashRefreshToken(refreshToken);

        String redisKey = "refresh:" + tokenHash;

        String username = refreshTokenRedisRepo.getUsername(redisKey);

//        String usedToken = refreshTokenRedisRepo.getUsedToken(tokenHash);


        if (username == null) {

            String usedToken = refreshTokenRedisRepo.getUsedToken(tokenHash);
            if (usedToken!=null)
            {
                refreshTokenRedisRepo.revokeFamilyId(usedToken);
                throw  new invalidRefreshTokenException("reject token as reused");
            }



            throw new invalidRefreshTokenException("invalid refresh token");
        }

        String familyId = refreshTokenRedisRepo.getFamilyId(redisKey);

        if (familyId == null) {
            throw new invalidRefreshTokenException("invalid token family");
        }


        String status = refreshTokenRedisRepo.getFamilyIdStatus(familyId);

        if (!"ACTIVE".equals(status)) {
            throw new invalidRefreshTokenException("token family is not active");
        }


        return username;
    }

    public String rotateRefreshToken(String oldRefreshToken) {
        String username = validateAndGetUsername(oldRefreshToken);
        String oldHash = hashRefreshToken(oldRefreshToken);

        String oldRedisKey = "refresh:" + oldHash;
        String familyId = refreshTokenRedisRepo.getFamilyId(oldRedisKey);
        refreshTokenRedisRepo.markTokenAsUsed(oldHash,familyId,Duration.ofDays(7));

        refreshTokenRedisRepo.delete(oldRedisKey);
        return createRefreshToken(username, familyId);
    }

}
