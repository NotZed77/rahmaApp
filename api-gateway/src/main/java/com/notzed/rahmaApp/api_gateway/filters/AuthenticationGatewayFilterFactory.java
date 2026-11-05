package com.notzed.rahmaApp.api_gateway.filters;

import com.notzed.rahmaApp.api_gateway.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final JwtService jwtService;

    public AuthenticationGatewayFilterFactory(JwtService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            if (path.startsWith("/identity/")) return chain.filter(exchange);

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            log.info("Authentication filter called for path: {}", path);
            log.info("Authorization header present: {}", authHeader != null);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return unauthorized(exchange);
            }

            String token = authHeader.substring(7).trim();

            try {
                String userId = jwtService.getUserIdFromToken(token);
                String role = jwtService.getRoleFromToken(token);

                log.info("JWT validated successfully");
                log.info("User ID: {}", userId);
                log.info("User Role: {}", role);

                ServerHttpRequest request = exchange.getRequest().mutate()
                        .headers(headers -> {
                            headers.remove("X-User-Id");
                            headers.remove("X-User-Role");
                            headers.set("X-User-Id", userId);
                            headers.set("X-User-Role", role);
                        })
                        .build();

                log.info("Before forwarding - User ID: {}", request.getHeaders().getFirst("X-User-Id"));

                log.info("Before forwarding - Role: {}", request.getHeaders().getFirst("X-User-Role"));

                ServerWebExchange modifiedExchange = exchange.mutate().request(request).build();

                return chain.filter(modifiedExchange);

            } catch (Exception ex) {
                log.error("JWT authentication failed: {}", ex.getMessage(), ex);
                return unauthorized(exchange);
            }
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}