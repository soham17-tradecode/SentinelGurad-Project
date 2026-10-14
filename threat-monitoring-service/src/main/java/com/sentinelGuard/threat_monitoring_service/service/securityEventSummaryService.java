package com.sentinelGuard.threat_monitoring_service.service;

import com.sentinelGuard.threat_monitoring_service.dto.securityEventSummaryDto;
import com.sentinelGuard.threat_monitoring_service.repo.securityEventRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
