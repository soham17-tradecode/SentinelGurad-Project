package com.sentinelGuard.threat_monitoring_service.controller;

import com.sentinelGuard.threat_monitoring_service.dto.securityEventSummaryDto;
import com.sentinelGuard.threat_monitoring_service.service.securityEventSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/security-events")
public class securityEventSummaryController {
    private final securityEventSummaryService service;

    @GetMapping("/summary")
    public securityEventSummaryDto getSummary()
    {
        return service.getSummary();
    }

}
