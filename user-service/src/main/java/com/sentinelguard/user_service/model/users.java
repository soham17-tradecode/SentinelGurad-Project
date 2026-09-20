package com.sentinelguard.user_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Data
@Table(name = "userTable")
@AllArgsConstructor
@NoArgsConstructor

public class users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NonNull
    String username;
    String email;
    String fullName;
    LocalDateTime createAt;
    LocalDateTime updateAt;

}
