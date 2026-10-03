package com.notzed.rahmaApp.user_service.service;

import com.notzed.rahmaApp.identity_service.dto.SignUpRequestDto;
import com.notzed.rahmaApp.user_service.auth.UserContextHolder;
import com.notzed.rahmaApp.user_service.client.IdentityFeignClient;
import com.notzed.rahmaApp.user_service.dto.ProfileUpdateRequestDto;
import com.notzed.rahmaApp.user_service.dto.UserDto;
import com.notzed.rahmaApp.user_service.dto.UserRequestDto;
import com.notzed.rahmaApp.user_service.entity.Gender;
import com.notzed.rahmaApp.user_service.entity.User;
import com.notzed.rahmaApp.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final IdentityFeignClient identityClient;

    @Transactional
    public UserRequestDto createNewUser(UserRequestDto userRequestDto){
        log.info("Creating a new user with name: {}", userRequestDto.getUserName());
        Long userId = UserContextHolder.getCurrentUserId();

        User user = modelMapper.map(userRequestDto, User.class);

        user.setUserId(userId);

        userRepository.save(user);
        log.info("Created a new user with ID: {}", user.getUserId());
        identityClient.signUp(
                new SignUpRequestDto()
        )

        return modelMapper.map(user, UserRequestDto.class);

    }

    public UserDto getUserById(Long userId){
        log.info("Getting the user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id: " + userId + "not found"));

        return modelMapper.map(user, UserDto.class);
    }
    
    public void updateProfile(Long userId, ProfileUpdateRequestDto profileUpdateRequestDto) {
        User user = userRepository.findById(userId).orElseThrow
                (() -> new RuntimeException("User with id: " + userId + "not found"));
        log.info("Getting the profile to be updated for user with ID: {}", userId);

        if(profileUpdateRequestDto.getDateOfBirth() != null) user.setDateOfBirth(profileUpdateRequestDto.getDateOfBirth());
        if(profileUpdateRequestDto.getGender() != null) user.setGender(profileUpdateRequestDto.getGender());
        if(profileUpdateRequestDto.getName() != null) user.setName(profileUpdateRequestDto.getName());

        log.info("Profile updated");
        userRepository.save(user);
    }


}
