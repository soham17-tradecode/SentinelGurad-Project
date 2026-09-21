package com.sentinelguard.user_service.controller;

import com.sentinelguard.user_service.DTO.userDTO;
import com.sentinelguard.user_service.DTO.userResponseDTO;
import com.sentinelguard.user_service.model.users;
import com.sentinelguard.user_service.services.userService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
public class simpleController {
    @Autowired
    userService userService;

    @GetMapping("/me")
    public String hello()
    {
        return "hi from now ";
    }

    @PostMapping("/users")
    public ResponseEntity<userResponseDTO> saveUsers(@Valid @RequestBody userDTO users)
    {
        users users1 = new users();
        users1.setUsername(users.getUsername());
        users1.setEmail(users.getEmail());
        users1.setFullName(users.getFullName());


        users savedUsers =  userService.setUser(users1);
        userResponseDTO response = new userResponseDTO();
        response.setId(savedUsers.getId());
        response.setUsername(savedUsers.getUsername());
        response.setEmail(savedUsers.getEmail());
        response.setFullName(savedUsers.getFullName());
        response.setCreateAt(savedUsers.getCreateAt());
        response.setUpdateAt(savedUsers.getUpdateAt());

         return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<users>> getUsers()
    {
        return ResponseEntity.ok(userService.allUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<userResponseDTO> findbyId(@PathVariable Long id)
    {

        userResponseDTO userResponseDTO = new userResponseDTO();
        users users = userService.findByid(id);



        userResponseDTO.setId(users.getId());
        userResponseDTO.setUsername(users.getUsername());
        userResponseDTO.setEmail(users.getEmail());
        userResponseDTO.setFullName(users.getFullName());
        userResponseDTO.setCreateAt(users.getCreateAt());
        userResponseDTO.setUpdateAt(users.getUpdateAt());


        return ResponseEntity.ok(userResponseDTO);




    }
    @PutMapping("/user/{id}")
    public ResponseEntity<users> update(@PathVariable Long id,@RequestBody users updateUser)
    {
        users update = userService.updateUsers(id,updateUser);
        return ResponseEntity.ok(update);


    }




    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteId(@PathVariable Long id)
    {
        boolean ok = userService.deleteUser(id);
        if (ok)
        {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }




    @GetMapping("/user/page")
    public ResponseEntity<Page<users>> getUsers(Pageable pageable)
    {
        return ResponseEntity.ok(userService.getUsers(pageable));
    }






    @GetMapping("/user/search")
    public ResponseEntity<Page<userResponseDTO>> searchUser(@RequestParam String username,Pageable pageable)
    {
        Page<userResponseDTO> result = userService.searchUser(pageable,username);

        return ResponseEntity.ok(result);







    }

}
