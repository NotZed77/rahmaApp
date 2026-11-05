package com.notzed.rahmaApp.identity_service.controller;

import com.notzed.rahmaApp.identity_service.dto.IdentityDto;
import com.notzed.rahmaApp.identity_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/promote/{userId}")
    public ResponseEntity<IdentityDto> promoteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.promoteUser(userId));
    }
}
