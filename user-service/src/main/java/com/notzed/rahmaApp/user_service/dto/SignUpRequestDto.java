package com.notzed.rahmaApp.user_service.dto;

import com.notzed.rahmaApp.user_service.entity.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpRequestDto {
    private String email;
    private String password;
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
}
