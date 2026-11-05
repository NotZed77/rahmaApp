package com.notzed.rahmaApp.user_service.dto;

import com.notzed.rahmaApp.user_service.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequestDto {
    private String userName;
    private LocalDate dateOfBirth;
    private Gender gender;
}