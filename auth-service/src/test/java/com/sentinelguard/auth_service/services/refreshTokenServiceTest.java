package com.sentinelguard.auth_service.services;

import com.sentinelguard.auth_service.DTO.refreshTokenRotationResponse;
import com.sentinelguard.auth_service.exception.invalidRefreshTokenException;
import com.sentinelguard.auth_service.repo.redis.refreshTokenConsumeResponse;
import com.sentinelguard.auth_service.repo.redis.refreshTokenConsumeResult;
import com.sentinelguard.auth_service.repo.redis.refreshTokenRedisRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class refreshTokenServiceTest {

    @Mock
    private refreshTokenRedisRepo refreshTokenRedisRepo;

    @InjectMocks
    private refreshTokenService refreshTokenService;

    @Test
    void rotateRefreshToken_shouldRotateSuccessfully()
    {
        String oldRefreshToken = "old-refresh-token";
        String username = "soham";
        String familyId = "family-123";

        refreshTokenConsumeResponse response =
                new refreshTokenConsumeResponse(
                        refreshTokenConsumeResult.SUCCESS,
                        username,
                        familyId
                );

        when(refreshTokenRedisRepo.consumeRefreshTokenAtomically(anyString()))
                .thenReturn(response);

        refreshTokenRotationResponse result =
                refreshTokenService.rotateRefreshToken(oldRefreshToken);


        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertNotNull(result.getRefreshToken());

        verify(refreshTokenRedisRepo)
                .consumeRefreshTokenAtomically(anyString());

        verify(refreshTokenRedisRepo)
                .save(
                        anyString(),
                        eq(username),
                        eq(familyId),
                        any()
                );
    }

    @Test
    void rotateRefreshToken_shouldRejectReusedToken() {

        String oldRefreshToken = "already-used-token";

        refreshTokenConsumeResponse response =
                new refreshTokenConsumeResponse(
                        refreshTokenConsumeResult.REUSED,
                        null,
                        null
                );

        when(refreshTokenRedisRepo.consumeRefreshTokenAtomically(anyString()))
                .thenReturn(response);

        invalidRefreshTokenException exception =
                assertThrows(
                        invalidRefreshTokenException.class,
                        () -> refreshTokenService.rotateRefreshToken(oldRefreshToken)
                );

        assertEquals(
                "refresh token reuse detected",
                exception.getMessage()
        );

        verify(refreshTokenRedisRepo)
                .consumeRefreshTokenAtomically(anyString());

        verify(refreshTokenRedisRepo, never())
                .save(
                        anyString(),
                        anyString(),
                        anyString(),
                        any()
                );
    }
    @Test
    void rotateRefreshToken_shouldRejectInvalidToken() {

        String invalidRefreshToken = "invalid-token";

        refreshTokenConsumeResponse response =
                new refreshTokenConsumeResponse(
                        refreshTokenConsumeResult.INVALID,
                        null,
                        null
                );

        when(refreshTokenRedisRepo.consumeRefreshTokenAtomically(anyString()))
                .thenReturn(response);

        invalidRefreshTokenException exception =
                assertThrows(
                        invalidRefreshTokenException.class,
                        () -> refreshTokenService.rotateRefreshToken(invalidRefreshToken)
                );

        assertEquals(
                "invalid refresh token",
                exception.getMessage()
        );

        verify(refreshTokenRedisRepo)
                .consumeRefreshTokenAtomically(anyString());

        verify(refreshTokenRedisRepo, never())
                .save(
                        anyString(),
                        anyString(),
                        anyString(),
                        any()
                );
    }

    @Test
    void rotateRefreshToken_shouldRejectRevokedFamily() {

        String refreshToken = "revoked-family-token";

        refreshTokenConsumeResponse response =
                new refreshTokenConsumeResponse(
                        refreshTokenConsumeResult.FAMILY_REVOKED,
                        null,
                        null
                );

        when(refreshTokenRedisRepo.consumeRefreshTokenAtomically(anyString()))
                .thenReturn(response);

        invalidRefreshTokenException exception =
                assertThrows(
                        invalidRefreshTokenException.class,
                        () -> refreshTokenService.rotateRefreshToken(refreshToken)
                );

        assertEquals(
                "token family is not active",
                exception.getMessage()
        );

        verify(refreshTokenRedisRepo)
                .consumeRefreshTokenAtomically(anyString());

        verify(refreshTokenRedisRepo, never())
                .save(
                        anyString(),
                        anyString(),
                        anyString(),
                        any()
                );
    }

    @Test
    void rotateRefreshToken_shouldRejectTransactionConflict() {

        String refreshToken = "conflicting-token";

        refreshTokenConsumeResponse response =
                new refreshTokenConsumeResponse(
                        refreshTokenConsumeResult.CONFLICT,
                        null,
                        null
                );

        when(refreshTokenRedisRepo.consumeRefreshTokenAtomically(anyString()))
                .thenReturn(response);

        invalidRefreshTokenException exception =
                assertThrows(
                        invalidRefreshTokenException.class,
                        () -> refreshTokenService.rotateRefreshToken(refreshToken)
                );

        assertEquals(
                "refresh token request conflict",
                exception.getMessage()
        );

        verify(refreshTokenRedisRepo)
                .consumeRefreshTokenAtomically(anyString());

        verify(refreshTokenRedisRepo, never())
                .save(
                        anyString(),
                        anyString(),
                        anyString(),
                        any()
                );
    }


}
