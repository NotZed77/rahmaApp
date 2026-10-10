package com.notzed.rahmaApp.prayer_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "prayers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime fajr;
    private LocalTime sunrise;
    private LocalTime dhuhr;
    private LocalTime asr;
    private LocalTime maghrib;
    private LocalTime isha;

    private LocalTime jummah;

    @ManyToOne
    @JoinColumn(name = "center_id")
    private Center center;

    private String location;

    private String timezone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private PrayerTimeSource source;
}

