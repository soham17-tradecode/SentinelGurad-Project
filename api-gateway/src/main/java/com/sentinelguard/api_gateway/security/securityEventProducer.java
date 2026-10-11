package com.sentinelguard.api_gateway.security;

import com.sentinelguard.api_gateway.security.securityEvent.securityEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
@Service
@RequiredArgsConstructor
public class securityEventProducer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    @PostConstruct
    public void initialize() {
        kafkaTemplate.partitionsFor(TOPIC);
    }

    private static final String TOPIC = "security-events";

    public void publish(securityEvent event)
    {
        kafkaTemplate.send(
                TOPIC,
                event.clientIp(),
                event
        );
    }
}



