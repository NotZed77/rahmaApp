package com.notzed.rahmaApp.user_service.controller;

import com.notzed.rahmaApp.user_service.dto.LoginDto;
import com.notzed.rahmaApp.user_service.dto.LoginResponseDto;
import com.notzed.rahmaApp.user_service.dto.SignUpRequestDto;
import com.notzed.rahmaApp.user_service.dto.UserDto;
import com.notzed.rahmaApp.user_service.service.AuthService;
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

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        UserDto userDto = authService.signUp(signUpRequestDto);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletResponse response, HttpServletRequest request){
        String token = authService.login(loginDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(String refreshToken, HttpServletRequest request){
        String accessToken = authService.refreshToken(refreshToken, request);
        return ResponseEntity.ok(accessToken);
    }
}
