package com.sentinelGuard.threat_monitoring_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "security_events")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

public class securityEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String events;

    @Column(nullable = false)
    private String clientIp;

    private String method;

    private String path;

    @Column(nullable = false, updatable = false)
    private LocalDateTime receivedAt;

    @PrePersist
    protected void onCreated() {
        receivedAt = LocalDateTime.now();
    }
}
