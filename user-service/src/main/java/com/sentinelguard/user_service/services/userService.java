package com.sentinelguard.user_service.services;

import com.sentinelguard.user_service.DTO.userDTO;
import com.sentinelguard.user_service.DTO.userResponseDTO;
import com.sentinelguard.user_service.exception.duplicateUserException;
import com.sentinelguard.user_service.exception.userNotFoundException;
import com.sentinelguard.user_service.model.users;
import com.sentinelguard.user_service.repo.userRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class userService {

    @Autowired
    userRepo userRepo;

    public users setUser(users user) //saving the users
    {
        if (userRepo.existsByUsername(user.getUsername()))
        {
            throw new duplicateUserException("Username already exists : "+user.getUsername());
        }

        if (userRepo.existsByEmail(user.getEmail()))
        {
            throw new duplicateUserException("Email already exists : "+user.getEmail());
        }
        return userRepo.save(user);
    }

    public List<userResponseDTO> allUsers() {
        List<users> userList = userRepo.findAll();
       return userList.stream().map(user -> {
            userResponseDTO response = new userResponseDTO();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setEmail(user.getEmail());
            response.setFullName(user.getFullName());
            response.setCreateAt(user.getCreateAt());
            response.setUpdateAt(user.getUpdateAt());

            return response;

        }).toList();
    }

    public users findByid(Long id) {
        return (userRepo.findById(id).orElseThrow(() ->
                new userNotFoundException("user not found with id : " + id)
        ));
    }

    public users updateUsers(Long id, userDTO updateUser) {
        users exists = findByid(id);
        if (userRepo.existsByUsernameAndIdNot(
                updateUser.getUsername(), id)) {

            throw new duplicateUserException(
                    "Username already exists: " + updateUser.getUsername()
            );
        }

        if (userRepo.existsByEmailAndIdNot(
                updateUser.getEmail(), id)) {

            throw new duplicateUserException(
                    "Email already exists: " + updateUser.getEmail()
            );
        }



        exists.setUsername(updateUser.getUsername());
        exists.setEmail(updateUser.getEmail());
        exists.setFullName(updateUser.getFullName());

        return userRepo.save(exists);

    }

    public void deleteUser(Long id) {
        users exists = findByid(id);

        userRepo.delete(exists);




    }

    //paginated service
    public Page<userResponseDTO> getUsers(Pageable pageable) {


        Page<users> userPage =  userRepo.findAll(pageable);

        return userPage.map(user ->{
            userResponseDTO response = new userResponseDTO();
            response.setId(user.getId());
            response.setUsername(user.getUsername());
            response.setEmail(user.getEmail());
            response.setFullName(user.getFullName());
            response.setCreateAt(user.getCreateAt());
            response.setUpdateAt(user.getUpdateAt());

            return response;

        } );
    }

    public Page<userResponseDTO> searchUser(Pageable pageable, String username) {
        Page<users> result = userRepo.findByUsernameContainingIgnoreCase(username, pageable);
        return result.map(users -> {

            userResponseDTO userResponseDTO = new userResponseDTO();

            userResponseDTO.setId(users.getId());
            userResponseDTO.setUsername(users.getUsername());
            userResponseDTO.setEmail(users.getEmail());
            userResponseDTO.setFullName(users.getFullName());
            userResponseDTO.setCreateAt(users.getCreateAt());
            userResponseDTO.setUpdateAt(users.getUpdateAt());
            return userResponseDTO;
        });
    }

}
