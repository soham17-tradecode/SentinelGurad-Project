package com.sentinelguard.auth_service.repo.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.jsontype.impl.StdTypeResolverBuilder;

import java.time.Duration;

@Repository
public class refreshTokenRedisRepo {
    private final StringRedisTemplate redisTemplate;


    public refreshTokenRedisRepo(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String key, String username, String familyId, Duration ttl) {


        redisTemplate.opsForHash().put(key,"username" ,username);
        redisTemplate.opsForHash().put(key,"familyId" ,familyId);

        redisTemplate.expire(key,ttl);
    }

    public String getUsername(String key)
    {
        Object username = redisTemplate.opsForHash().get(key,"username");
        if (username==null)
        {
            return null;
        }
        return username.toString();

    }

    public String getFamilyId(String key)
    {
        Object familyID = redisTemplate.opsForHash().get(key,"familyId");

        if (familyID == null)
        {
            return  null;
        }
        return familyID.toString();
    }

    public void delete(String key)
    {
        redisTemplate.delete(key);
    }

    public void familyIdStatus(String familyId)
    {
        String key = "family:"+familyId;

        redisTemplate.opsForValue().set(key,"ACTIVE");
    }

    public String getFamilyIdStatus(String familyId)
    {
        String key = "family:"+familyId;

        return redisTemplate.opsForValue().get(key);
    }

    public void revokeFamilyId(String familyId)
    {
        String key = "family:"+familyId;
        redisTemplate.opsForValue().set(key,"REVOKED");
    }

    public void markTokenAsUsed(String tokenHash,String familyId,Duration ttl)
    {
        String redisToken = "used:"+tokenHash;

        redisTemplate.opsForValue().set(redisToken,familyId);
        redisTemplate.expire(redisToken,ttl);
    }
    public String getUsedToken(String tokenHash)
    {
        String usedToken = "used:"+tokenHash;

       return redisTemplate.opsForValue().get(usedToken);
    }
}
