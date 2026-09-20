package com.sentinelguard.user_service.services;

import com.sentinelguard.user_service.model.users;
import com.sentinelguard.user_service.repo.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userService {

    @Autowired
    userRepo userRepo;

    public void setUser(users user) //saving the users
    {
        userRepo.save(user);
    }
    public List<users> allUsers()
    {
        return userRepo.findAll();
    }

}
