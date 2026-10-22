package com.sentinelGuard.threat_monitoring_service.service;

import com.sentinelGuard.threat_monitoring_service.entity.adminIpAuditEntity;
import com.sentinelGuard.threat_monitoring_service.repo.adminIpAuditRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class adminIpAuditService {
    private final adminIpAuditRepo adminIpAuditRepo;

    @Transactional
    public adminIpAuditEntity recordAction(  Long adminUserId,
                                             String adminUsername,
                                             String action,
                                             String targetIp,
                                             String reason,
                                             String outcome)
    {
        adminIpAuditEntity audit = adminIpAuditEntity.builder()
                .adminUserId(adminUserId)
                .adminUsername(adminUsername)
                .targetIp(targetIp)
                .reason(reason)
                .action(action)
                .outcome(outcome)
                .build();

        return adminIpAuditRepo.save(audit);

    }

    @Transactional (readOnly = true)
    public List<adminIpAuditEntity> getAuditHistory()
    {
        return adminIpAuditRepo.findAllByOrderByCreatedAtDesc();
    }



}
