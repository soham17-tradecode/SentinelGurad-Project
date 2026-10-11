package com.sentinelGuard.threat_monitoring_service.consumer;

import com.sentinelGuard.threat_monitoring_service.entity.securityEventEntity;
import com.sentinelGuard.threat_monitoring_service.event.securityEvents;
import com.sentinelGuard.threat_monitoring_service.repo.securityEventRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class securityEventConsumers {
    private final securityEventRepo repo;

    @KafkaListener(
            topics = "security-events",
            groupId = "threat-monitoring-group")
    public void consume(securityEvents events)
    {
        securityEventEntity securityEvent = new securityEventEntity();

        securityEvent.setEvents(events.event());
        securityEvent.setMethod(events.method());
        securityEvent.setClientIp(events.clientIp());
        securityEvent.setPath(events.path());

        securityEventEntity savedEvents = repo.save(securityEvent);






        log.info("security event received : id={},events={},clientIp={}",
                savedEvents.getId(),
                savedEvents.getEvents(),
                savedEvents.getClientIp()
        );

    }
}
