package com.notzed.rahmaApp.notification_service.repository;

import com.notzed.rahmaApp.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Notification> findByNotificationIdAndUserId(Long notificationId, Long userId);
    long countByUserIdAndIsReadFalse(Long userId);
}