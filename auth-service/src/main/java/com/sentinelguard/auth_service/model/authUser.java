package com.sentinelguard.auth_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table (name = "auth_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class authUser {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false ,unique = true)
    private String username;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private role role;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createAt;

    @PrePersist
    protected  void onCreate()
    {
        createAt = LocalDateTime.now();
    }


}
