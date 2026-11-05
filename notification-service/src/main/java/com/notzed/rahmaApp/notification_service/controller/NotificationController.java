package com.notzed.rahmaApp.notification_service.controller;

import com.notzed.rahmaApp.notification_service.dto.CreateNotificationDto;
import com.notzed.rahmaApp.notification_service.dto.NotificationDto;
import com.notzed.rahmaApp.notification_service.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(@Valid @RequestBody CreateNotificationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.createNotification(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<List<NotificationDto>> getMyNotifications(Authentication authentication) {
        log.info("Controller authenticated user ID: {}", authentication.getName());
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(notificationService.getMyNotifications(userId));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long notificationId, Authentication authentication) {
        log.info("Controller authenticated user ID: {}", authentication.getName());
        Long userId = Long.parseLong(authentication.getName());
        log.info("User ID: {}", userId);
        return ResponseEntity.ok(notificationService.markAsRead(notificationId, userId));
    }

    @GetMapping("/me/unread-count")
    public ResponseEntity<Long> getUnreadCount(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }
}
