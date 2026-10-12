package com.notzed.rahmaApp.event_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventSearchRequest {

    private String eventAt;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer page = 0;

    private Integer size = 10;
}
