package com.sentinelguard.user_service.services;

import com.sentinelguard.user_service.DTO.userResponseDTO;
import com.sentinelguard.user_service.model.users;
import com.sentinelguard.user_service.repo.userRepo;
import lombok.Lombok;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class userService {

    @Autowired
    userRepo userRepo;

    public users setUser(users user) //saving the users
    {
       return userRepo.save(user);
    }
    public List<users> allUsers()
    {
        return userRepo.findAll();
    }

    public Optional<users> findByid(Long id)
    {
        return userRepo.findById(id);
    }

    public Optional<users> updateUsers(Long id,users updateUser)
    {
        Optional<users> exists = findByid(id);
        if (exists.isPresent())
        {
            users existingUser = exists.get();

            existingUser.setUsername(updateUser.getUsername());
            existingUser.setEmail(updateUser.getEmail());

            setUser(existingUser);

            return Optional.of(existingUser);
        }
        return Optional.empty();

    }
    public boolean deleteUser(Long id)
    {
        Optional<users> exists = findByid(id);
        if (exists.isPresent())
        {
            userRepo.deleteById(id);
            return true;
        }

        return false;

    }

}
