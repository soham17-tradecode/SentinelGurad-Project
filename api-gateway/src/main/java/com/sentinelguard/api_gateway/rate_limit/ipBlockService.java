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

        return Boolean.TRUE.equals(redisTemplate.hasKey("blocked_ip:auto:" + clientIp))
        || Boolean.TRUE.equals(redisTemplate.hasKey("blocked_ip:manual:" + clientIp));


    }

    public void blockIp(String clientIp) {
       redisTemplate.opsForValue().set("blocked_ip:manual:"+clientIp,"blocked");
    }

    public void unBlockIp(String clientIp) {
        redisTemplate.delete("blocked_ip:manual:"+clientIp);

    }

    public void blockIpAutomatically(String clientIp)
    {
        redisTemplate.opsForValue().set("blocked_ip:auto:"+clientIp,"blocked");
    }

    public void unBlockIpAutomatically(String clientIp)
    {
        redisTemplate.delete("blocked_ip:auto:"+clientIp);
    }
}
