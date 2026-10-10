package com.notzed.rahmaApp.prayer_service.dto;

import com.notzed.rahmaApp.prayer_service.entity.Center;
import com.notzed.rahmaApp.prayer_service.entity.PrayerTimeSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrayerDto {
    private Long id;
    private LocalDate date;
    private LocalTime fajr;
    private LocalTime sunrise;
    private LocalTime dhuhr;
    private LocalTime asr;
    private LocalTime maghrib;
    private LocalTime isha;
    private LocalTime jummah;
    private Double latitude;
    private Double longitude;
    private String location;
    private String timezone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PrayerTimeSource source;
}
