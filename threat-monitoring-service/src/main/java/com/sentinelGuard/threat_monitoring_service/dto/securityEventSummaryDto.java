package com.sentinelGuard.threat_monitoring_service.dto;

public record securityEventSummaryDto(long totalEvents, long failedEvents, long rateLimitExceeded, long otherEvents) {
}
