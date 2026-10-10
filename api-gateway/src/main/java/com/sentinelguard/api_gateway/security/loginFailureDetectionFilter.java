package com.sentinelguard.api_gateway.security;

import com.sentinelguard.api_gateway.rate_limit.securityAuditService;
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


@Component
@RequiredArgsConstructor
public class loginFailureDetectionFilter implements GlobalFilter, Ordered {

    private final threatDetectionService threatDetectionService;
    private final securityAuditService securityAuditService;
    private final threatResponseService threatResponseService;


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




                    }



                }));
    }

    private  securityEvent getSecurityEvent (ServerWebExchange exchange, String path) {
        String clientIp = exchange.getRequest().
                getRemoteAddress()
                .getAddress().
                getHostAddress();

        if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
            clientIp = "127.24.0.1";
        }

        securityEvent securityEvent = new securityEvent(
                "LOGIN_FAILED",
                clientIp,
                exchange.getRequest().getMethod().name(),
                path
        );

        threatScore threatScore = threatDetectionService.detect(securityEvent);

        if ("CRITICAL".equals(threatScore.level()))
        {
            threatResponseService.blockIpTemporarily(clientIp);
            securityAuditService.logSecurityEvent("THREAT_CRITICAL",
                    clientIp,
                    exchange);

        }
        return securityEvent;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
