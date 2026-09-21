package com.sentinelguard.user_service.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class userDTO {
    @NotBlank(message = "username cannot blank")
    private String username;
    @NotBlank(message = "email cannot be blank")
    @Email(message = "provide a valid email")
    private String email;
    @NotBlank(message = "Full name cannot blank")
    private String fullName;
}
