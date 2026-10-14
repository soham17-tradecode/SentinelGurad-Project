package com.sentinelGuard.threat_monitoring_service.service;

import com.sentinelGuard.threat_monitoring_service.dto.securityEventSummaryDto;
import com.sentinelGuard.threat_monitoring_service.repo.securityEventRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class securityEventSummaryService {

    private final securityEventRepo securityEventRepo;


    public securityEventSummaryDto getSummary() {
        long totalEvents = securityEventRepo.count();

        long failedEvents = securityEventRepo.countByEvents("LOGIN_FAILED");

        long rateLimitExceeded = securityEventRepo.countByEvents("RATE_LIMIT_EXCEEDED");

        long otherEvents = totalEvents - failedEvents - rateLimitExceeded;

        return new securityEventSummaryDto(totalEvents,failedEvents,rateLimitExceeded,otherEvents);
    }

    public securityEventSummaryDto getSummary(long hours)
    {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(hours);

        long totalEvent = securityEventRepo.countByReceivedAtAfter(cutoff);

        long failedEvents = securityEventRepo.countByEventsAndReceivedAtAfter("LOGIN_FAILED",cutoff);
        long rateLimitExceeded = securityEventRepo.countByEventsAndReceivedAtAfter("RATE_LIMIT_EXCEEDED",cutoff);

        long otherEvent = totalEvent - failedEvents - rateLimitExceeded;

        return new securityEventSummaryDto(totalEvent,failedEvents,rateLimitExceeded,otherEvent);
    }


}
