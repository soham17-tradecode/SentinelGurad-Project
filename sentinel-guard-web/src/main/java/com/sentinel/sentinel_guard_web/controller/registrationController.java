package com.sentinel.sentinel_guard_web.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Controller
public class RegistrationController {

    private final RestClient restClient;

    public RegistrationController(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String fullName,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (fullName.isBlank() || username.isBlank() || email.isBlank()
                || password.isBlank() || confirmPassword.isBlank()) {
            model.addAttribute("registerError", "Please complete every field.");
            return "register";
        }

        if (username.trim().length() < 3) {
            model.addAttribute("registerError", "Username must contain at least 3 characters.");
            return "register";
        }

        if (password.length() < 8) {
            model.addAttribute("registerError", "Password must contain at least 8 characters.");
            return "register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("registerError", "The passwords do not match.");
            return "register";
        }

        try {
            restClient.post()
                    .uri("/auth/register")
                    .body(Map.of(
                            "fullName", fullName.trim(),
                            "username", username.trim(),
                            "email", email.trim(),
                            "password", password))
                    .retrieve()
                    .toBodilessEntity();

            redirectAttributes.addFlashAttribute(
                    "registerSuccess", "Account created successfully. You can now sign in.");
            return "redirect:/login";

        } catch (RestClientResponseException ex) {
            String message;
            if (ex.getStatusCode() == HttpStatus.CONFLICT) {
                message = "That username or email is already registered.";
            } else if (ex.getStatusCode().is4xxClientError()) {
                message = "Registration was rejected. Check your details and try again.";
            } else {
                message = "The registration service is temporarily unavailable. Try again later.";
            }
            model.addAttribute("registerError", message);
            return "register";
        } catch (Exception ex) {
            model.addAttribute("registerError",
                    "Could not reach the registration service. Please try again later.");
            return "register";
        }
    }
}
