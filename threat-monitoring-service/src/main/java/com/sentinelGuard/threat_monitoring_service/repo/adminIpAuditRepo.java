package com.sentinelGuard.threat_monitoring_service.repo;

import com.sentinelGuard.threat_monitoring_service.entity.adminIpAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface adminIpAuditRepo extends JpaRepository<adminIpAuditEntity , Long> {

    List<adminIpAuditEntity> findAllByOrderByCreatedAtDesc();
}
