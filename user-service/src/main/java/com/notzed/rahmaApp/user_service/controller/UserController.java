package com.notzed.rahmaApp.user_service.controller;

import com.notzed.rahmaApp.user_service.dto.LoginDto;
import com.notzed.rahmaApp.user_service.dto.ProfileUpdateRequestDto;
import com.notzed.rahmaApp.user_service.dto.SignUpRequestDto;
import com.notzed.rahmaApp.user_service.dto.UserDto;
import com.notzed.rahmaApp.user_service.entity.User;
import com.notzed.rahmaApp.user_service.service.AuthService;
import com.notzed.rahmaApp.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId){
        UserDto userDto = userService.getUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.FOUND);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateProfile(@PathVariable Long userId, @RequestBody ProfileUpdateRequestDto profileUpdateRequestDto){
        userService.updateProfile(userId, profileUpdateRequestDto);
        return ResponseEntity.noContent().build();
    }


}
