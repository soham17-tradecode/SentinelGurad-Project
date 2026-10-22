package com.sentinelGuard.threat_monitoring_service.dto;

public record adminIpAuditRequest(
        Long adminUserId,
        String adminUsername,
        String action,
        String targetIp,
        String reason,
        String outcome
) {
}
