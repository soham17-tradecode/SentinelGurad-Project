package com.sentinelGuard.threat_monitoring_service.specification;

import com.sentinelGuard.threat_monitoring_service.entity.securityEventEntity;
import org.springframework.data.jpa.domain.Specification;

public class securityEventSpecification {
    public static Specification<securityEventEntity> hasEventType(String eventType)
    {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("events"),eventType));
    }
}
