package com.sentinelGuard.threat_monitoring_service.repo;

import com.sentinelGuard.threat_monitoring_service.entity.securityEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface securityEventRepo extends JpaRepository<securityEventEntity, Long>, JpaSpecificationExecutor<securityEventEntity> {

    long countByEvents(String events);
    long countByEventsIn(java.util.Collection<String> events);

    long countByReceivedAtAfter(LocalDateTime cutoff);
    long countByEventsAndReceivedAtAfter(String events , LocalDateTime cutoff);
}
