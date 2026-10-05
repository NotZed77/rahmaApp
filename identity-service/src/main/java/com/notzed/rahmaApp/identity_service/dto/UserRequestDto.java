package com.notzed.rahmaApp.identity_service.dto;

import com.notzed.rahmaApp.identity_service.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
    private Long identityId;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private Gender gender;

}
