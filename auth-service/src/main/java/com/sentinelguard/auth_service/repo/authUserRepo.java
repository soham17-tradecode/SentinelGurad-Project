package com.sentinelguard.auth_service.repo;

import com.sentinelguard.auth_service.model.authUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;

public interface authUserRepo extends JpaRepository<authUser,Long> {
    Optional<authUser> findByUsername (String username);
    Optional<authUser> findByEmail (String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
