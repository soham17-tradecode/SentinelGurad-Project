package com.sentinelguard.api_gateway.rate_limit;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micrometer.core.instrument.MeterRegistry;

@Service
@RequiredArgsConstructor

public class securityAuditService {

    private final MeterRegistry meterRegistry;
    private static final Logger log = LoggerFactory.getLogger(securityAuditService.class);


    public void logSecurityEvent(String event, String clientIp, ServerWebExchange exchange) {

        HttpMethod method = exchange.getRequest().getMethod();
        String path = exchange.getRequest().getPath().value();

        log.warn("SECURITY_EVENT={} IP={} METHOD={} PATH={}",event,clientIp,method,path);
        meterRegistry.counter("sentinel_security_events_total","event",event).increment();

    }
}
