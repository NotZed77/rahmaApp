package com.notzed.rahmaApp.identity_service.controller;

import com.notzed.rahmaApp.identity_service.dto.LoginDto;
import com.notzed.rahmaApp.identity_service.dto.LoginResponseDto;
import com.notzed.rahmaApp.identity_service.dto.SignUpRequestDto;
import com.notzed.rahmaApp.identity_service.dto.SignUpResponseDto;
import com.notzed.rahmaApp.identity_service.service.AuthService;
import com.notzed.rahmaApp.identity_service.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto response = authService.signUp(signUpRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {

        LoginResponseDto tokens = authService.login(loginDto);

        Cookie cookie = new Cookie("refreshToken", tokens.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30);

        response.addCookie(cookie);

        return ResponseEntity.ok(tokens.getAccessToken());
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request){
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SignUpResponseDto> getIdentityId(@PathVariable Long id){
        return new ResponseEntity<>(authService.getIdentityId(id), HttpStatus.OK);
    }
}