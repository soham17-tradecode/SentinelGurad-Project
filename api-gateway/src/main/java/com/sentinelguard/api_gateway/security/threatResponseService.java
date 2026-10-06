package com.sentinelguard.api_gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class threatResponseService {
    private final StringRedisTemplate stringRedisTemplate;

    public void blockIpTemporarily(String clientIp)
    {
        String key = "blocked_ip:" + clientIp;

        stringRedisTemplate.opsForValue().set(key,"blocked", Duration.ofMinutes(10));
    }
}
