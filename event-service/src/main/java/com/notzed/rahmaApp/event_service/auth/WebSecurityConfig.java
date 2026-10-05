package com.notzed.rahmaApp.event_service.auth;

import com.notzed.rahmaApp.event_service.auth.filters.UserAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserAuthenticationFilter userAuthenticationFilter;

    public WebSecurityConfig(UserAuthenticationFilter userAuthenticationFilter) {
        this.userAuthenticationFilter = userAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/core/**").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}