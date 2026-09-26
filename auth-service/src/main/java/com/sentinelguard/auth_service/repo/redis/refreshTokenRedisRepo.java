package com.sentinelguard.auth_service.repo.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

import javax.swing.text.StyledEditorKit;
import java.time.Duration;
import java.util.List;

@Repository
public class refreshTokenRedisRepo {
    private final StringRedisTemplate redisTemplate;


    public refreshTokenRedisRepo(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String key, String username, String familyId, Duration ttl) {


        redisTemplate.opsForHash().put(key, "username", username);
        redisTemplate.opsForHash().put(key, "familyId", familyId);

        redisTemplate.expire(key, ttl);
    }

    public String getUsername(String key) {
        Object username = redisTemplate.opsForHash().get(key, "username");
        if (username == null) {
            return null;
        }
        return username.toString();

    }

    public String getFamilyId(String key) {
        Object familyID = redisTemplate.opsForHash().get(key, "familyId");

        if (familyID == null) {
            return null;
        }
        return familyID.toString();
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void familyIdStatus(String familyId) {
        String key = "family:" + familyId;

        redisTemplate.opsForValue().set(key, "ACTIVE");
    }

    public String getFamilyIdStatus(String familyId) {
        String key = "family:" + familyId;

        return redisTemplate.opsForValue().get(key);
    }

    public void revokeFamilyId(String familyId) {
        String key = "family:" + familyId;
        redisTemplate.opsForValue().set(key, "REVOKED");
    }

    public void markTokenAsUsed(String tokenHash, String familyId, Duration ttl) {
        String redisToken = "used:" + tokenHash;

        redisTemplate.opsForValue().set(redisToken, familyId);
        redisTemplate.expire(redisToken, ttl);
    }

    public String getUsedToken(String tokenHash) {
        String usedToken = "used:" + tokenHash;

        return redisTemplate.opsForValue().get(usedToken);
    }

    //concurrency
    public refreshTokenConsumeResponse consumeRefreshTokenAtomically(String tokenHash) {

        String refreshKey = "refresh:" + tokenHash;
        String usedKey = "used:" + tokenHash;

        for (int attempt = 0; attempt < 3; attempt++) {

            List<Object> result = redisTemplate.execute(new SessionCallback<List<Object>>() {

                @Override
                public List<Object> execute(RedisOperations operations) throws DataAccessException {

                    operations.watch(refreshKey);

                    String username = (String) operations.opsForHash()
                            .get(refreshKey, "username");

                    String familyId = (String) operations.opsForHash()
                            .get(refreshKey, "familyId");

                    if (username == null || familyId == null) {

                        String usedFamilyId = (String) operations.opsForValue()
                                .get(usedKey);

                        operations.unwatch();

                        if (usedFamilyId != null) {

                            refreshTokenRedisRepo.this.revokeFamilyId(usedFamilyId);

                            return List.of("REUSED");
                        }

                        return List.of("INVALID");
                    }

                    String familyStatus = (String) operations.opsForValue()
                            .get("family:" + familyId);

                    if (!"ACTIVE".equals(familyStatus)) {

                        operations.unwatch();

                        return List.of("FAMILY_REVOKED");
                    }

                    operations.multi();

                    operations.opsForValue()
                            .set(usedKey, familyId);

                    operations.expire(
                            usedKey,
                            Duration.ofDays(7)
                    );

                    operations.delete(refreshKey);

                    List<Object> execResult = operations.exec();

                    if (execResult == null) {
                        return null;
                    }

                    return List.of(
                            "SUCCESS",
                            username,
                            familyId
                    );
                }
            });

            /*
             * Another request modified the watched key.
             * Retry the transaction.
             */
            if (result == null) {
                continue;
            }

            String status = (String) result.get(0);

            if ("SUCCESS".equals(status)) {

                return new refreshTokenConsumeResponse(
                        refreshTokenConsumeResult.SUCCESS,
                        (String) result.get(1),
                        (String) result.get(2)
                );
            }

            if ("REUSED".equals(status)) {

                return new refreshTokenConsumeResponse(
                        refreshTokenConsumeResult.REUSED,
                        null,
                        null
                );
            }

            if ("FAMILY_REVOKED".equals(status)) {

                return new refreshTokenConsumeResponse(
                        refreshTokenConsumeResult.FAMILY_REVOKED,
                        null,
                        null
                );
            }

            if ("INVALID".equals(status)) {

                return new refreshTokenConsumeResponse(
                        refreshTokenConsumeResult.INVALID,
                        null,
                        null
                );
            }
        }

        /*
         * Three consecutive transaction conflicts.
         */
        return new refreshTokenConsumeResponse(
                refreshTokenConsumeResult.CONFLICT,
                null,
                null
        );
    }
}
