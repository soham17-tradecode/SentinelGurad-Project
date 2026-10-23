package com.sentinel.sentinel_guard_web.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String welcome() {
        return "index";
    }


    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        model.addAttribute(
                "username",
                session.getAttribute("SG_USERNAME")
        );

        model.addAttribute(
                "role",
                session.getAttribute("SG_ROLE")
        );

        return "dashboard";
    }
}
