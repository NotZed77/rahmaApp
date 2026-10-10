package com.notzed.rahmaApp.prayer_service.service;

import com.notzed.rahmaApp.prayer_service.dto.AlAdhanMonthlyResponseDto;
import com.notzed.rahmaApp.prayer_service.dto.AlAdhanResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrayerService {

    private final RestClient restClient;

    @Cacheable(cacheNames = "prayer_time", key = "T(java.time.LocalDate).now(T(java.time.ZoneId).of('America/Winnipeg'))")
    public AlAdhanResponseDto getPrayerTimeForToday(){
        LocalDate today = LocalDate.now(ZoneId.of("America/Winnipeg"));
        log.info("Getting prayers timing for today with Date: {}", today);
        return getPrayerTime(today);
    }

    @Cacheable(cacheNames = "prayer_time_date", key = "#date")
    public AlAdhanResponseDto getPrayerTimeForDate(LocalDate date){
        return getPrayerTime(date);
    }

    @Cacheable(cacheNames = "prayer_time_month", key = "#month")
    public AlAdhanMonthlyResponseDto getPrayerTimeForMonth(Year year, Month month) {
        return getPrayerTimeMonthly(year, month);
    }

    @CacheEvict(cacheNames = "prayer_time", allEntries = true)
    public void evictPrayerTime(){}

    @CacheEvict(cacheNames = "prayer_time_date", allEntries = true)
    public void evictPrayerTimeDate(){}

    @CacheEvict(cacheNames = "prayer_time_month", allEntries = true)
    public void evictMonthPrayerTime(){}


    public AlAdhanMonthlyResponseDto getPrayerTimeMonthly(Year year, Month month){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.aladhan.com")
                        .path("/v1/calendar/{year}/{month}")
                        .queryParam("latitude", 49.8951)
                        .queryParam("longitude", -97.1384)
                        .queryParam("method", 2)
                        .queryParam("school", 0)
                        .queryParam("midnightMode", 0)
                        .queryParam("timezonestring", "America/Winnipeg")
                        .queryParam("latitudeAdjustmentMethod", 1)
                        .queryParam("calendarMethod", "UAQ")
                        .queryParam("iso8601", false)
                        .build(year.getValue(), month.getValue()))
                .retrieve()
                .body(AlAdhanMonthlyResponseDto.class);
    }

    public AlAdhanResponseDto getPrayerTime(LocalDate date){
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.aladhan.com")
                        .path("/v1/timings/{date}")
                        .queryParam("latitude", 49.8951)
                        .queryParam("longitude", -97.1384)
                        .queryParam("method", 2)
                        .queryParam("school", 0)
                        .queryParam("midnightMode", 0)
                        .queryParam("timezonestring", "America/Winnipeg")
                        .queryParam("latitudeAdjustmentMethod", 1)
                        .queryParam("calendarMethod", "UAQ")
                        .queryParam("iso8601", false)
                        .build(date))
                .retrieve()
                .body(AlAdhanResponseDto.class);
    }
}
