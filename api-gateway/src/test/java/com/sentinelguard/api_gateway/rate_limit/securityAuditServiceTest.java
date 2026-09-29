package com.sentinelguard.api_gateway.rate_limit;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;
import org.springframework.http.server.RequestPath;
public class securityAuditServiceTest {

    @Test
    void logSecurity_shouldCompleteSuccessfully()
    {
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        ServerHttpRequest request = mock(ServerHttpRequest.class);
        RequestPath requestPath = mock(RequestPath.class);

        when(exchange.getRequest()).thenReturn(request);
        when(request.getMethod()).thenReturn(HttpMethod.GET);
        when(request.getPath()).thenReturn(requestPath);
        when(requestPath.value()).thenReturn("/users/1");

        securityAuditService service = new securityAuditService();
        assertDoesNotThrow(()->
                service.logSecurityEvent("IP_BLOCKED","127.0.0.1",exchange));
    }
}
