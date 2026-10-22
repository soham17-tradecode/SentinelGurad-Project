
package com.sentinelguard.api_gateway.controller;

import com.sentinelguard.api_gateway.rate_limit.ipBlockService;
import com.sentinelguard.api_gateway.security.auditClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/ip-blocks")
public class ipBlockController {
    private static final Logger log =
            LoggerFactory.getLogger(ipBlockController.class);

    private final ipBlockService ipBlockService;
    private final auditClient auditClient;

    @PostMapping("/{ip}")
    public Mono<ResponseEntity<Map<String, String>>> blocksIp(
            @PathVariable String ip,
            @RequestBody ipBlockRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        if (request.reason() == null || request.reason().isBlank()) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A reason is required"));
        }

        return Mono.fromRunnable(() -> ipBlockService.blockIp(ip))
                .then(Mono.defer(() -> auditClient.recordAction(
                        Map.of(
                                "adminUserId", Long.valueOf(
                                        jwt.getClaimAsString("userId")),
                                "adminUsername", jwt.getSubject(),
                                "action", "BLOCK_IP",
                                "targetIp", ip,
                                "reason", request.reason(),
                                "outcome", "SUCCESS"
                        )
                )))
                .thenReturn(ResponseEntity.ok(
                        Map.of("message", "IP address manually blocked", "ip", ip)))
                .onErrorMap(error -> new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "IP block or audit recording failed", error));
    }


    @DeleteMapping("/{ip}")
    public Mono<ResponseEntity<Map<String, String>>> unBlocksIp(
            @PathVariable String ip,
            @RequestBody ipBlockRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        if (request.reason() == null || request.reason().isBlank()) {
            return Mono.error(new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A reason is required"));
        }

        return Mono.fromRunnable(() -> ipBlockService.unBlockIp(ip))
                .then(Mono.defer(() -> auditClient.recordAction(
                        Map.of(
                                "adminUserId", Long.valueOf(
                                        jwt.getClaimAsString("userId")),
                                "adminUsername", jwt.getSubject(),
                                "action", "UNBLOCK_IP",
                                "targetIp", ip,
                                "reason", request.reason(),
                                "outcome", "SUCCESS"
                        )
                )))
                .thenReturn(ResponseEntity.ok(
                        Map.of(
                                "message", "Manual IP block removed",
                                "ip", ip
                        )
                ))
                .doOnError(error ->
                        log.error("UNBLOCK IP OR AUDIT FAILED for {}", ip, error))
                .onErrorMap(error -> new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "IP unblock or audit recording failed",
                        error));
    }


    @GetMapping("/{ip}")
    public Mono<ResponseEntity<Map<String, Object>>> checkIp(
            @PathVariable String ip) {

        return Mono.fromSupplier(() -> ResponseEntity.ok(
                Map.of("ip", ip, "blocked", ipBlockService.isIpBlocked(ip))));
    }
}
