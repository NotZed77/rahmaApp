    package com.notzed.rahmaApp.prayer_service.repository;

import com.notzed.rahmaApp.prayer_service.entity.PrayerTiming;
import jakarta.websocket.ClientEndpointConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface PrayerRepository extends JpaRepository<PrayerTiming, Long> {
    Optional<PrayerTiming> findByDateAndLocationKey(LocalDate date, String locationKey);

    List<PrayerTiming> findByDateBetweenAndLocationKeyOrderByDateAsc(LocalDate startDate, LocalDate endDate, String locationKey);

    Optional<PrayerTiming> findByYearAndMonthAndLocation(Year year, Month month, String locationKey);
}
