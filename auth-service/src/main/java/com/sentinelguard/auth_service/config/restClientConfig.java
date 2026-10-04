package com.sentinelguard.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class restClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}