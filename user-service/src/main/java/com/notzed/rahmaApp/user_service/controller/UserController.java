package com.notzed.rahmaApp.user_service.controller;

import com.notzed.rahmaApp.user_service.dto.ProfileUpdateRequestDto;
import com.notzed.rahmaApp.user_service.dto.UserDto;
import com.notzed.rahmaApp.user_service.dto.UserRequestDto;
import com.notzed.rahmaApp.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserRequestDto> createNewUser(@RequestBody UserRequestDto userRequestDto){
        log.info("User service controller reached");
        UserRequestDto userRequestDto1 = userService.createNewUser(userRequestDto);
        return new ResponseEntity<>(userRequestDto1, HttpStatus.CREATED);
    }

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
