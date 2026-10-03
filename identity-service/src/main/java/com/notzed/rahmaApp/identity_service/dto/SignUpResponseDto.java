package com.notzed.rahmaApp.identity_service.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponseDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long identityId;
}
