package com.notzed.rahmaApp.user_service.client;

import com.notzed.rahmaApp.identity_service.dto.SignUpRequestDto;
import com.notzed.rahmaApp.user_service.dto.SignUpResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "IDENTITY-SERVICE")
public interface IdentityFeignClient {

    @PostMapping("/auth/signup")
    ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto);
}
