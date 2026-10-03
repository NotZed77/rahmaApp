package com.notzed.rahmaApp.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth

                        .pathMatchers("/api/v1/users/admin/**")
                        .hasRole("ADMIN")

                        .pathMatchers("/api/v1/events/**")
                        .hasAnyRole("STAFF", "ADMIN")

                        .anyExchange().permitAll()
                )
                .build();
    }
}


