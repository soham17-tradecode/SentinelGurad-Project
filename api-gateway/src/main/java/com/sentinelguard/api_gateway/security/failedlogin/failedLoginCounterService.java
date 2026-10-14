package com.sentinelguard.api_gateway.security.failedlogin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class failedLoginCounterService {

    private final StringRedisTemplate redisTemplate;

    private static final int THRESHOLD = 5;
    private static final Duration WINDOW = Duration.ofMinutes(10);

    public int recordFailedLogin(String clientIp)
    {
        String key = "failed_key:"+clientIp;

        Long count = redisTemplate.opsForValue().increment(key);
        if (count == null)
        {
            throw new IllegalStateException("could not increment failed-login counter");
        }
        if (count==1){
            redisTemplate.expire(key,WINDOW);
        }

        return count.intValue();
    }

    public boolean isThresholdExceeded(int count)
    {
        return count>=THRESHOLD;
    }
}
