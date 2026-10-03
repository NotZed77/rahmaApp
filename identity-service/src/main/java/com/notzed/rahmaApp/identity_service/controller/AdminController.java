package com.notzed.rahmaApp.identity_service.controller;

import com.notzed.rahmaApp.identity_service.dto.IdentityDto;
import com.notzed.rahmaApp.identity_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/promote")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<IdentityDto> promoteUser(Long userId){
        IdentityDto identityDto = adminService.promoteUser(userId);
        return new ResponseEntity<>(identityDto, HttpStatus.ACCEPTED);
    }
}
