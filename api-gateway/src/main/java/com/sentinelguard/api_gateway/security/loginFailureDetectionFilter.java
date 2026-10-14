package com.sentinelguard.api_gateway.security;

import com.sentinelguard.api_gateway.rate_limit.securityAuditService;
import com.sentinelguard.api_gateway.security.failedlogin.failedLoginCounterService;
import com.sentinelguard.api_gateway.security.securityEvent.securityEvent;
import com.sentinelguard.api_gateway.security.threatDetectionService.threatDetectionService;
import com.sentinelguard.api_gateway.security.threatScore.threatScore;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class loginFailureDetectionFilter implements GlobalFilter, Ordered {

    private final threatDetectionService threatDetectionService;
    private final securityAuditService securityAuditService;
    private final threatResponseService threatResponseService;
    private final securityEventProducer securityEventProducer;
    private final failedLoginCounterService failedLoginCounterService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {

                    HttpStatusCode status = exchange.getResponse().getStatusCode();

                    String path = exchange.getRequest()
                            .getPath()
                            .value();

                    if ("/auth/login".equals(path) && status != null && status.value() == 401) {
                        securityEvent securityEvent = getSecurityEvent(exchange, path);
                        securityEventProducer.publish(securityEvent);


                    }


                }));
    }

    private securityEvent getSecurityEvent(ServerWebExchange exchange, String path) {
        String clientIp = exchange.getRequest().
                getRemoteAddress()
                .getAddress().
                getHostAddress();

        log.info("Gateway observed client IP: {}", clientIp);

        if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
            clientIp = "127.24.0.1";
        }

        securityEvent securityEvent = new securityEvent(
                "LOGIN_FAILED",
                clientIp,
                exchange.getRequest().getMethod().name(),
                path
        );


        int failedAttempt = failedLoginCounterService.recordFailedLogin(clientIp);
        boolean thresholdExceeded = failedLoginCounterService.isThresholdExceeded(failedAttempt);


        threatScore threatScore = threatDetectionService.detect(securityEvent);
        boolean criticalThreat = "CRITICAL".equals(threatScore.level());

        //threasholdLevel
        if (thresholdExceeded) {
            securityAuditService.logSecurityEvent("FAILED_LOGIN_THRESHOLD_EXCEEDED",
                    clientIp,
                    exchange
            );

        }

        //criticallevel--->
        if ("CRITICAL".equals(threatScore.level())) {
            securityAuditService.logSecurityEvent("THREAT_CRITICAL",
                    clientIp,
                    exchange);

        }
        if (thresholdExceeded || criticalThreat) {
            threatResponseService.blockIpTemporarily(clientIp);
        }

        return securityEvent;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
