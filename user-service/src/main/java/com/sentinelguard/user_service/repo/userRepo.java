package com.sentinelguard.user_service.repo;

import com.sentinelguard.user_service.model.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepo extends JpaRepository<users,Long> {

}
