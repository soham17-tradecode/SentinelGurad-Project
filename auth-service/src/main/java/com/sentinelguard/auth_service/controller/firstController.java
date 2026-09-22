package com.sentinelguard.auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/auth")
public class firstController {
    @GetMapping("/me")
    public String first()
    {
        return "hello auth service";
    }
    //only for testing this is not auth branch of git
    // this is api-gateway branch .
    //before merging to main delete this file and merge it to main........
}
