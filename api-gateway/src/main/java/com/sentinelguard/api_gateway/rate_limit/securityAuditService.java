package com.sentinelguard.api_gateway.rate_limit;


import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service

public class securityAuditService {
    private static final Logger log = LoggerFactory.getLogger(securityAuditService.class);


    public void logSecurityEvent(String event, String clientIp, ServerWebExchange exchange) {

        HttpMethod method = exchange.getRequest().getMethod();
        String path = exchange.getRequest().getPath().value();

        log.warn("SECURITY_EVENT={} IP={} METHOD={} PATH={}",event,clientIp,method,path);

    }
}
