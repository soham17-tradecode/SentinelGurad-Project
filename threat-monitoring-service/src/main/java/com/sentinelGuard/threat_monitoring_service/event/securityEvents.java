package com.sentinelGuard.threat_monitoring_service.event;

public record securityEvents(String event, String clientIp, String method, String path) {
}
