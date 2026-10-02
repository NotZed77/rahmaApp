package com.notzed.rahmaApp.user_service.service;

import com.notzed.rahmaApp.user_service.auth.UserContextHolder;
import com.notzed.rahmaApp.user_service.dto.*;
import com.notzed.rahmaApp.user_service.entity.*;
import com.notzed.rahmaApp.user_service.exception.*;
import com.notzed.rahmaApp.user_service.repository.*;
import com.notzed.rahmaApp.user_service.util.PasswordUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signUp(SignUpRequestDto signUpRequestDto){
        boolean exists = userRepository.existsByEmail(signUpRequestDto.getEmail());
        log.info("Signing up");

        if(exists){
            throw new RuntimeException("User is already present with same email ID");
        }

        User newUser = modelMapper.map(signUpRequestDto, User.class);
        newUser.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));

        User savedUser = userRepository.save(newUser);

        log.info("Successfully signed up");
        return modelMapper.map(savedUser, UserDto.class);
    }

    public String login(LoginDto loginDto){
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: "+ loginDto.getEmail()));

        log.info("Logging in");
        boolean isPasswordOfMatch = PasswordUtil.checkPassword(loginDto.getPassword(), user.getPassword());

        if(!isPasswordOfMatch){
            throw new BadRequestException("Incorrect Password");
        }

        return jwtService.generateAccessToken(user);
    }

    public String refreshToken(String refreshToken, HttpServletRequest request){
        Long id = jwtService.getUserIdFromToken(refreshToken);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: "+id));

        refreshToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new BadRequestException("Refresh Token not found inside the Cookies"));

        return jwtService.generateRefreshToken(user);
    }
}
