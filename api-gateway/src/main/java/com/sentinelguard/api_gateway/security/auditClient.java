package com.sentinelguard.api_gateway.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component

public class auditClient {

    private final WebClient webClient;
    private final String auditInternalSecret;

    public auditClient(
            WebClient.Builder webClientBuilder,
            @Value("${THREAT_MONITORING_SERVICE_URL:http://localhost:8084}")
            String threatMonitoringUrl,
            @Value("${audit.internal-secret}")
            String auditInternalSecret) {

        this.webClient = webClientBuilder
                .baseUrl(threatMonitoringUrl)
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE)
                .build();

        this.auditInternalSecret = auditInternalSecret;
    }

    public Mono<Void> recordAction(Map<String, Object> auditRecord) {
        return webClient.post()
                .uri("/api/admin/ip-audit")
                .header("X-Audit-Secret", auditInternalSecret)
                .bodyValue(auditRecord)
                .retrieve()
                .bodyToMono(Void.class);
    }


}
