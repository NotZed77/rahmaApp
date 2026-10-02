package com.notzed.rahmaApp.event_service.dto;

import com.notzed.rahmaApp.event_service.entity.EventContactInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventDto {
    private Long ownerId;
    private Long eventId;
    private String eventName;
    private LocalDateTime eventTime;
    private Boolean active;
    private EventContactInfo contactInfo;
}
