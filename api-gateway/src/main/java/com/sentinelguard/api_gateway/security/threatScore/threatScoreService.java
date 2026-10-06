package com.sentinelguard.api_gateway.security.threatScore;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class threatScoreService {

    private final StringRedisTemplate template;

    public int addScore(String clientIP,int points)
    {
        String key = "threat_score:"+clientIP;
        Long score = template.opsForValue().increment(key,points);

        if (score == null)
        {
            throw new IllegalStateException("could not update threat score");
        }
        template.expire(key, Duration.ofMinutes(10));
        return score.intValue();

    }

    public void resetScore(String clientIp)
    {
        String key = "threat_score:"+clientIp;

        template.delete(key);
    }
}
