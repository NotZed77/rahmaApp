package com.notzed.rahmaApp.notification_service.service;

import com.notzed.rahmaApp.notification_service.dto.CreateNotificationDto;
import com.notzed.rahmaApp.notification_service.dto.NotificationDto;
import com.notzed.rahmaApp.notification_service.entity.Notification;
import com.notzed.rahmaApp.notification_service.exception.ResourceNotFoundException;
import com.notzed.rahmaApp.notification_service.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ModelMapper modelMapper;

    public NotificationDto createNotification(CreateNotificationDto dto) {
        log.info("Creating notification for user ID: {}", dto.getUserId());

        Notification notification = Notification.builder()
                .userId(dto.getUserId())
                .title(dto.getTitle())
                .message(dto.getMessage())
                .type(dto.getType())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        log.info("Notification {} saved for user ID: {}",
                savedNotification.getNotificationId(),
                savedNotification.getUserId());

        return modelMapper.map(savedNotification, NotificationDto.class);
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> getMyNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .toList();
    }

    public NotificationDto markAsRead(Long notificationId, Long userId) {
        log.info("User ID from service: {}", userId);
        log.info("Noti ID from service: {}", notificationId);
        Notification notification = notificationRepository.findByNotificationIdAndUserId(notificationId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));

        if (!Boolean.TRUE.equals(notification.getIsRead())) {
            notification.setIsRead(true);
            notification.setReadAt(LocalDateTime.now());
        }

        return modelMapper.map(notificationRepository.save(notification), NotificationDto.class);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }
}
