package com.sentinelguard.user_service.controller;

import com.sentinelguard.user_service.model.users;
import com.sentinelguard.user_service.services.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class simpleController {
    @Autowired
    userService userService;

    @GetMapping("/me")
    public String hello()
    {
        return "hi from now ";
    }

    @PostMapping("/save")
    public void saveUsers(@RequestBody users users)
    {
        users.setCreateAt(LocalDateTime.now());
        users.setUpdateAt(LocalDateTime.now());
        userService.setUser(users);
    }

    @GetMapping("/get")
    public ResponseEntity<List<users>> getUsers()
    {
        return ResponseEntity.ok(userService.allUsers());
    }
}
