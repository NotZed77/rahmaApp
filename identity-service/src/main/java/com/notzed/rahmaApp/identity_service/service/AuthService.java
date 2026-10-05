package com.notzed.rahmaApp.identity_service.service;

import com.notzed.rahmaApp.identity_service.client.UserFeignClient;
import com.notzed.rahmaApp.identity_service.dto.LoginDto;
import com.notzed.rahmaApp.identity_service.dto.SignUpRequestDto;
import com.notzed.rahmaApp.identity_service.dto.SignUpResponseDto;
import com.notzed.rahmaApp.identity_service.dto.UserRequestDto;
import com.notzed.rahmaApp.identity_service.entity.Identity;
import com.notzed.rahmaApp.identity_service.entity.Role;
import com.notzed.rahmaApp.identity_service.exception.ResourceNotFoundException;
import com.notzed.rahmaApp.identity_service.repository.IdentityRepository;
import com.notzed.rahmaApp.identity_service.util.PasswordUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final IdentityRepository identityRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final UserFeignClient userClient;


    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto){
        boolean exists = identityRepository.existsByEmail(signUpRequestDto.getEmail());
        log.info("Signing up");

        if(exists){
            throw new RuntimeException("User is already present with same email ID");
        }

        Identity newIdentity = modelMapper.map(signUpRequestDto, Identity.class);
        newIdentity.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));
        newIdentity.setRole(Role.USER);


        Identity savedIdentity = identityRepository.save(newIdentity);

        log.info("Identity saved, calling User Service...");
        userClient.createUser(
                new UserRequestDto(
                        savedIdentity.getId(),
                        signUpRequestDto.getName(),
                        signUpRequestDto.getEmail(),
                        signUpRequestDto.getDateOfBirth(),
                        signUpRequestDto.getGender()
                )
        );

        log.info("Successfully signed up");
        return modelMapper.map(savedIdentity, SignUpResponseDto.class);
    }

    @Transactional
    public String login(LoginDto loginDto){
        Identity identity = identityRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: "+ loginDto.getEmail()));

        log.info("Logging in");
        boolean isPasswordOfMatch = PasswordUtil.checkPassword(loginDto.getPassword(), identity.getPassword());

        if(!isPasswordOfMatch){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid credentials");
        }

        return jwtService.generateAccessToken(identity);
    }

    public String refreshToken(String refreshToken, HttpServletRequest request){
        Long id = jwtService.getUserIdFromToken(refreshToken);

        Identity identity = identityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: "+id));

        refreshToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new BadRequestException("Refresh Token not found inside the Cookies"));

        return jwtService.generateRefreshToken(identity);
    }

    public SignUpResponseDto getIdentityId(Long identityId){
        Identity identity = identityRepository.findById(identityId)
                .orElseThrow(() -> new RuntimeException("Identity not found with ID: "+ identityId));
        return modelMapper.map(identity, SignUpResponseDto.class);
    }

}
