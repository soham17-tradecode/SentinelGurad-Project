
package com.sentinel.sentinel_guard_web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class authenticationController {

    private final RestClient restClient;
    private final JwtDecoder jwtDecoder;
    private final SecurityContextRepository securityContextRepository;

    private static final Logger log =
            LoggerFactory.getLogger(authenticationController.class);

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        if (username.isBlank() || password.isBlank()) {
            redirectAttributes.addFlashAttribute(
                    "loginError",
                    "Enter your username and password."
            );
            return "redirect:/login";
        }

        try {
            // Authenticate against the Auth Service.
            TokenResponse tokens = restClient.post()
                    .uri("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "username", username,
                            "password", password
                    ))
                    .retrieve()
                    .body(TokenResponse.class);

            if (tokens == null
                    || tokens.accessToken() == null
                    || tokens.accessToken().isBlank()) {

                redirectAttributes.addFlashAttribute(
                        "loginError",
                        "Authentication service returned an invalid response."
                );
                return "redirect:/login";
            }

            // Validate JWT signature, issuer, and expiry.
            Jwt jwt = jwtDecoder.decode(tokens.accessToken());

            String roleClaim = jwt.getClaimAsString("role");
            String authority = normalizeRole(roleClaim);

            List<SimpleGrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority(authority)
            );

            AbstractAuthenticationToken authentication =
                    new JwtAuthenticationToken(
                            jwt,
                            authorities,
                            jwt.getSubject()
                    );

            // Rotate the session ID to mitigate session fixation.
            HttpSession existingSession = request.getSession(false);

            if (existingSession != null) {
                request.changeSessionId();
            }

            // Create or retrieve the authenticated session.
            HttpSession session = request.getSession(true);

            session.setAttribute(
                    "SG_ACCESS_TOKEN",
                    tokens.accessToken()
            );

            // Keep the refresh token server-side.
            if (tokens.refreshToken() != null
                    && !tokens.refreshToken().isBlank()) {

                session.setAttribute(
                        "SG_REFRESH_TOKEN",
                        tokens.refreshToken()
                );
            }

            session.setAttribute("SG_USERNAME", jwt.getSubject());
            session.setAttribute("SG_ROLE", authority);

            // Create the authenticated SecurityContext.
            SecurityContext context =
                    SecurityContextHolder.createEmptyContext();

            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // Persist the context using the configured repository.
            securityContextRepository.saveContext(
                    context,
                    request,
                    response
            );

            // Store it under Spring Security's standard session key.
            session.setAttribute(
                    HttpSessionSecurityContextRepository
                            .SPRING_SECURITY_CONTEXT_KEY,
                    context
            );

            // Open the protected dashboard after successful login.
            return "redirect:/dashboard";

        } catch (RestClientResponseException ex) {

            // Avoid exposing Auth Service response details to the browser.
            log.warn(
                    "Authentication service rejected login with HTTP status {}",
                    ex.getStatusCode()
            );

            redirectAttributes.addFlashAttribute(
                    "loginError",
                    "Invalid username or password."
            );

            return "redirect:/login";

        } catch (JwtException ex) {

            log.warn("JWT validation failed during login", ex);

            redirectAttributes.addFlashAttribute(
                    "loginError",
                    "Sign-in could not be verified. Contact the system administrator."
            );

            return "redirect:/login";

        } catch (RuntimeException ex) {

            log.error(
                    "Unexpected error during frontend login processing",
                    ex
            );

            redirectAttributes.addFlashAttribute(
                    "loginError",
                    "Login failed. Check the application logs."
            );

            return "redirect:/login";
        }
    }

    private String normalizeRole(String roleClaim) {

        if (roleClaim == null || roleClaim.isBlank()) {
            throw new JwtException("Required role claim is missing");
        }

        String normalized = roleClaim.startsWith("ROLE_")
                ? roleClaim
                : "ROLE_" + roleClaim;

        if (!normalized.equals("ROLE_USER")
                && !normalized.equals("ROLE_ADMIN")) {

            throw new JwtException("Unsupported role claim");
        }

        return normalized;
    }

    private record TokenResponse(
            String accessToken,
            String refreshToken
    ) {
    }
}
