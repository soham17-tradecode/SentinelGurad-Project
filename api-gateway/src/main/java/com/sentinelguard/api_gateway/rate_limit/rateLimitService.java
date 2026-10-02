package com.sentinelguard.api_gateway.rate_limit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class rateLimitService {

    private final StringRedisTemplate redisTemplate;




    public boolean isRequestAllowed(String clientIp) {
        String key = "rate_limit:" + clientIp;

        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(60));
        }
        if (count > 5) {
            return false;
        }

        return true;

    }


}
