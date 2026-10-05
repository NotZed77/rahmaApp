package com.notzed.rahmaApp.identity_service.controller;

import com.notzed.rahmaApp.identity_service.dto.LoginDto;
import com.notzed.rahmaApp.identity_service.dto.SignUpRequestDto;
import com.notzed.rahmaApp.identity_service.dto.SignUpResponseDto;
import com.notzed.rahmaApp.identity_service.service.AuthService;
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
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto response = authService.signUp(signUpRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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

    @GetMapping("/{id}")
    public ResponseEntity<SignUpResponseDto> getIdentityId(@PathVariable Long id){
        return new ResponseEntity<>(authService.getIdentityId(id), HttpStatus.OK);
    }
}