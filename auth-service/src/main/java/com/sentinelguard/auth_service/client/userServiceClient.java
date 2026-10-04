package com.sentinelguard.auth_service.client;

import com.sentinelguard.auth_service.DTO.userProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class userServiceClient {

    private final RestClient restClient;

    @Value("${USER_SERVICE_URL:http://localhost:8083}")
    private String userServiceUrl;

    public void createUserProfile(Long authUserId, String username, String email, String fullName) {

        restClient.post()
                .uri(userServiceUrl + "/users")
                .header("X-Auth-User-Id", authUserId.toString())
                .body(new userProfileRequest(
                        username,
                        email,
                        fullName
                ))
                .retrieve()
                .toBodilessEntity();
    }
}