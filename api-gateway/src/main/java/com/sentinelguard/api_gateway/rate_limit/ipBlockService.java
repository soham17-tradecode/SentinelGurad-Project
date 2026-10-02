package com.sentinelguard.api_gateway.rate_limit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service

public class ipBlockService {
    private final StringRedisTemplate redisTemplate;

    public ipBlockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isIpBlocked(String clientIp) {

        String key = "blocked_ip:" + clientIp;

        return redisTemplate.hasKey(key);



    }
    public void blockIp(String clientIp)
    {
        String key = "blocked_ip:" + clientIp;

        redisTemplate.opsForValue().set(key,"blocked");
    }

    public void unBlockIp(String clientIp)
    {
        String key = "blocked_ip:" + clientIp;
        redisTemplate.delete(key);

    }
}
