package com.sentinelGuard.threat_monitoring_service.controller;

import com.sentinelGuard.threat_monitoring_service.dto.adminIpAuditRequest;
import com.sentinelGuard.threat_monitoring_service.entity.adminIpAuditEntity;
import com.sentinelGuard.threat_monitoring_service.service.adminIpAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/ip-audit")
public class adminAuditController {

    private final adminIpAuditService adminIpAuditService;

    @Value("${AUDIT_INTERNAL_SECRET}")
    private String auditInternalSecret;

    @GetMapping
    public ResponseEntity<List<adminIpAuditEntity>> getAuditHistory() {

        return ResponseEntity.ok(adminIpAuditService.getAuditHistory());
    }

    @PostMapping
    public ResponseEntity<adminIpAuditEntity> recordEntity( @RequestHeader("X-Audit-Secret") String suppliedSecret,@RequestBody adminIpAuditRequest request)
    {
        if (!java.security.MessageDigest.isEqual(
                auditInternalSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                suppliedSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Invalid internal audit secret");
        }
        adminIpAuditEntity savedAudit = adminIpAuditService.recordAction(request.adminUserId(),
                request.adminUsername(),
                request.action(),
                request.targetIp(),
                request.reason(),
                request.outcome());

        return ResponseEntity.ok(savedAudit);
    }
}
