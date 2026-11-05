package com.notzed.rahmaApp.notification_service.dto;

import com.notzed.rahmaApp.notification_service.entity.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationDto {
    @NotNull private Long userId;
    @NotBlank private String title;
    @NotBlank private String message;
    @NotNull private NotificationType type;
}