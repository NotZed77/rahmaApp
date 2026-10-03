package com.notzed.rahmaApp.identity_service.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}
