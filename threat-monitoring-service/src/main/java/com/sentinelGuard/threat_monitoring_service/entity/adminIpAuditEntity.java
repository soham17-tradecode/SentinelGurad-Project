package com.sentinelGuard.threat_monitoring_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "admin_ip_audit")
@Builder
public class adminIpAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private Long adminUserId;

    @Column (nullable = false)
    private String adminUsername;

    @Column (nullable = false)
    private String action;

    @Column (nullable = false)
    private String targetIp;

    @Column (length = 500)
    private String reason;

    @Column (nullable = false)
    private String outcome;

    @Column (nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void  onCreated()
    {
        createdAt = LocalDateTime.now();
    }




}
