package com.sentinelguard.api_gateway.security.threatDetectionService;

import com.sentinelguard.api_gateway.security.securityEvent.securityEvent;
import com.sentinelguard.api_gateway.security.threatScore.threatScore;
import com.sentinelguard.api_gateway.security.threatScore.threatScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class threatDetectionService {
    private final threatScoreService threatScoreService;


    private int calculateScore(securityEvent event) {
        return switch (event.event()) {
            case "LOGIN_FAILED" -> 30;

            case "RATE_LIMIT_EXCEEDED" -> 20;

            case "401", "403" -> 10;

            default -> 0;
        };
    }
    private String determineLevel(int score)
    {
        if (score>=90)
        {
            return "CRITICAL";
        }
        if (score>=60)
        {
            return "HIGH";
        }
        if (score>=30){
            return "MEDIUM";
        }
        return "LOW";

    }
    public threatScore detect (securityEvent event)
    {
        int score = calculateScore(event);
        int totalScore = threatScoreService.addScore(event.clientIp(), score);

        String level = determineLevel(totalScore);

        return new threatScore(totalScore,level);
    }


}
