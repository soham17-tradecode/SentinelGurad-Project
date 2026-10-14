package com.sentinelGuard.threat_monitoring_service.controller;

import com.sentinelGuard.threat_monitoring_service.dto.securityEventSummaryDto;
import com.sentinelGuard.threat_monitoring_service.service.securityEventSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/security-events")
public class securityEventSummaryController {
    private final securityEventSummaryService service;

    @GetMapping("/summary")
    public securityEventSummaryDto getSummary(@RequestParam(required = false) Long hours) {
        if (hours == null) {
            return service.getSummary();
        }

        if (hours <= 0 || hours >= 720) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hours must be between 1 and 720");

        }

        return service.getSummary(hours);
    }

}
