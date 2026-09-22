package com.sentinelguard.user_service.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class userResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
