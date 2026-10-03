package com.sentinelguard.api_gateway.rate_limit;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component

public class rateLimitFilter implements GlobalFilter, Ordered {
    private final rateLimitService rateLimitService;
    private final ipBlockService ipBlockService;
    private final securityAuditService securityAuditService;

    public rateLimitFilter(rateLimitService rateLimitService, ipBlockService ipBlockService, securityAuditService securityAuditService) {
        this.rateLimitService = rateLimitService;
        this.ipBlockService = ipBlockService;
        this.securityAuditService = securityAuditService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientIp = exchange.getRequest()
                .getRemoteAddress()
                .getAddress()
                .getHostAddress();



        if ("0:0:0:0:0:0:0:1".equals(clientIp)) {
            clientIp = "127.0.0.1";
        }

        boolean blocked = ipBlockService.isIpBlocked(clientIp);


        if (blocked) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            securityAuditService.logSecurityEvent("IP_BLOCKED", clientIp, exchange);
            return exchange.getResponse().setComplete();
        }

        boolean allowed = rateLimitService.isRequestAllowed(clientIp);
        if (allowed) {
            return chain.filter(exchange);
        }
        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        securityAuditService.logSecurityEvent("RATE_LIMIT_EXCEEDED", clientIp, exchange);


        return exchange.getResponse().setComplete();

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
