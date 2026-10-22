package com.sentinelGuard.threat_monitoring_service.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class securityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
    {
        return http.csrf(csrf->csrf.disable())
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/admin/ip-audit"
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/admin/ip-audit"
                        ).hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/security-events/**")
                        .hasAuthority("ROLE_ADMIN")
                        .anyRequest().denyAll()

                ).oauth2ResourceServer(oauth2->oauth2
                        .jwt(jwt->jwt
                                .jwtAuthenticationConverter(jwtConverter())))

                .build();
    }
    @Bean
    public Converter<Jwt,? extends AbstractAuthenticationToken> jwtConverter()
    {

        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return converter;

    }
}
