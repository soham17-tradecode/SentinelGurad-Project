package com.sentinelGuard.threat_monitoring_service.repo;

import com.sentinelGuard.threat_monitoring_service.entity.securityEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface securityEventRepo extends JpaRepository<securityEventEntity, Long> {
}
