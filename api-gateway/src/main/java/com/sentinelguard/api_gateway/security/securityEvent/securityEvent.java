package com.sentinelguard.api_gateway.security.securityEvent;

public record securityEvent(String event,String clientIp,String method ,String path) {
}
