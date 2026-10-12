package com.notzed.rahmaApp.prayer_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;

@Entity
@Table(name = "prayers", uniqueConstraints = @UniqueConstraint(columnNames = {"prayer_date", "location_key"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrayerTiming {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prayer_date", nullable = false)
    private LocalDate date;

    @Column(name = "prayer_year")
    private Year year;

    @Column(name = "prayer_month")
    private Month month;

    @Column(name="location_key", nullable = false)
    private String locationKey;

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

    private LocalDateTime fetchedAt;
}

