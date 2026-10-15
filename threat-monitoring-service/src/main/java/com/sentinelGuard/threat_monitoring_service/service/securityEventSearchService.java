package com.sentinelGuard.threat_monitoring_service.service;

import com.sentinelGuard.threat_monitoring_service.entity.securityEventEntity;
import com.sentinelGuard.threat_monitoring_service.repo.securityEventRepo;
import com.sentinelGuard.threat_monitoring_service.specification.securityEventSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class securityEventSearchService {

    private final securityEventRepo repo;

    public Page<securityEventEntity> searchByEventType(String eventType, Pageable pageable)
    {
        Specification<securityEventEntity> specification = securityEventSpecification.hasEventType(eventType);

        return repo.findAll(specification,pageable);
    }
}
