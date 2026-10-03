package com.notzed.rahmaApp.user_service.dto;

import com.notzed.rahmaApp.user_service.entity.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {
    private Long identityId;
    private String userName;
    private String email;
    private LocalDate dateOfBirth;
    private Gender gender;
}
