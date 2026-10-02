package com.notzed.rahmaApp.user_service.dto;

import com.notzed.rahmaApp.user_service.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private UserRole userRoles;
}
