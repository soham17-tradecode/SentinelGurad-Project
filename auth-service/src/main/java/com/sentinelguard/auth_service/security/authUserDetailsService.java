package com.sentinelguard.auth_service.security;

import com.sentinelguard.auth_service.model.authUser;
import com.sentinelguard.auth_service.repo.authUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class authUserDetailsService implements UserDetailsService {

    private final authUserRepo authUserRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        authUser authUser = authUserRepo.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("User not found")
                );

        return User.builder()
                .username(authUser.getUsername())
                .password(authUser.getPassword())
                .roles(authUser.getRole().name())
                .build();


    }
}
