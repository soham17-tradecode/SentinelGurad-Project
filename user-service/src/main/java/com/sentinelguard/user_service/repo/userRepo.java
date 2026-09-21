package com.sentinelguard.user_service.repo;

import com.sentinelguard.user_service.model.users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface userRepo extends JpaRepository<users,Long> {

    Page<users> findByUsernameContainingIgnoreCase(String username,Pageable pageable);

}
