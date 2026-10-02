package com.notzed.rahmaApp.user_service.dto;


import com.notzed.rahmaApp.user_service.entity.Gender;
import lombok.*;
import java.time.LocalDate;

@Data
@Builder
public class ProfileUpdateRequestDto {
    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;
}