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
@RequiredArgsConstructor
public class rateLimitFilter implements GlobalFilter, Ordered {
    private final rateLimitService rateLimitService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientIp = exchange.getRequest()
                .getRemoteAddress()
                .getAddress()
                .getHostAddress();

        boolean allowed = rateLimitService.isRequestAllowed(clientIp);
        if (allowed)
        {
            return chain.filter(exchange);
        }
        else {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
