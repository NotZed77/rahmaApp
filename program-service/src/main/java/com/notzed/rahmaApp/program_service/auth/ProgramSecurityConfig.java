package com.notzed.rahmaApp.program_service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ProgramSecurityConfig {

    private final GatewayAuthenticationFilter gatewayAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.GET, "/core", "/core/*").permitAll()

                        .requestMatchers(HttpMethod.POST, "/core").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/core/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/core/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/core/*/status").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/core/enrollments").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/core/enrollments/*/cancel").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/core/*/enrollments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/core/users/*/enrollments").hasAnyRole("USER", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/class", "/class/*").permitAll()

                        .requestMatchers(HttpMethod.POST, "/class").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/class/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/class/*").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .addFilterBefore(gatewayAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public FilterRegistrationBean<GatewayAuthenticationFilter> filterRegistration(
            GatewayAuthenticationFilter filter) {

        FilterRegistrationBean<GatewayAuthenticationFilter> registration =
                new FilterRegistrationBean<>(filter);

        registration.setEnabled(false);
        return registration;
    }
}