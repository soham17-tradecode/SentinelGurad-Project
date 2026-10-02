package com.sentinelguard.api_gateway.rate_limit;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class securityAuditServiceTest {

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter counter;

    @InjectMocks
    private securityAuditService service;

    @Test
    void logSecurity_shouldCompleteSuccessfully() {

        ServerWebExchange exchange = mock(ServerWebExchange.class);
        ServerHttpRequest request = mock(ServerHttpRequest.class);
        RequestPath requestPath = mock(RequestPath.class);

        when(exchange.getRequest()).thenReturn(request);
        when(request.getMethod()).thenReturn(HttpMethod.GET);
        when(request.getPath()).thenReturn(requestPath);
        when(requestPath.value()).thenReturn("/users/1");

        when(meterRegistry.counter(
                "sentinel_security_events_total",
                "event",
                "IP_BLOCKED"
        )).thenReturn(counter);

        assertDoesNotThrow(() ->
                service.logSecurityEvent(
                        "IP_BLOCKED",
                        "127.0.0.1",
                        exchange
                )
        );

        verify(counter).increment();
    }
}