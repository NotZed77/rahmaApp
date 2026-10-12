package com.notzed.rahmaApp.prayer_service.service;

import com.notzed.rahmaApp.prayer_service.dto.*;
import com.notzed.rahmaApp.prayer_service.entity.PrayerTimeSource;
import com.notzed.rahmaApp.prayer_service.entity.PrayerTiming;
import com.notzed.rahmaApp.prayer_service.exception.PrayerTimingsUnavailableException;
import com.notzed.rahmaApp.prayer_service.repository.PrayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrayerService {

    private final RestClient restClient;
    private final PrayerRepository prayerRepository;

    @Cacheable(cacheNames = "prayer_time", key = "T(java.time.LocalDate).now(T(java.time.ZoneId).of('America/Winnipeg'))")
    public AlAdhanResponseDto getPrayerTimeForToday(){
        LocalDate today = LocalDate.now(ZoneId.of("America/Winnipeg"));
        return getAlAdhanResponseDto(today);
    }

    @NonNull
    private AlAdhanResponseDto getAlAdhanResponseDto(LocalDate today) {
        AlAdhanResponseDto responseDto;
        try {
            responseDto = getPrayerTime(today);
            validateResponse(responseDto);

        } catch (RestClientException | IllegalStateException ex) {
            log.warn("AlAdhan unavailable for {}. Using PostgreSQL.", today, ex);

            return getPrayerTimeFromDatabase(today);
        }
        try {
            savePrayerTime(today, responseDto);
        } catch (DataAccessException ex) {
            log.error("Could not persist prayer timings for {}", today, ex);
        }
        return responseDto;
    }

    @Cacheable(cacheNames = "prayer_time_date", key = "#date")
    public AlAdhanResponseDto getPrayerTimeForDate(LocalDate date) {
        return getAlAdhanResponseDto(date);
    }

    @Cacheable(cacheNames = "prayer_time_month", key = "#year.value + '-' + #month.value")
    public AlAdhanMonthlyResponseDto getPrayerTimeForMonth(Year year, Month month) {
        AlAdhanMonthlyResponseDto response;
        try{
            response = getPrayerTimeMonthly(year, month);
            validateMonthlyResponse(response);
        }catch (RestClientException | IllegalStateException ex){
            log.warn("AlAdhan unavailable for {} and {}, using PostgreSQL.", year, month, ex);
            return getPrayerTimeMonthlyFromDatabase(year, month);
        }
        try{
            savePrayerTimeMonthly(year, month, response);
        }catch (DataAccessException ex){
            log.error("Could not persist data the timings for {} {}", year, month, ex);
        }
        return response;
    }

    private void validateMonthlyResponse(AlAdhanMonthlyResponseDto response) {
        List<AlAdhanData> days = response.getData();
        for(AlAdhanData day: days){
            if(day == null || day.getTimings() == null){
                throw new IllegalStateException("A day is missing its prayer timings");
            }
            AlAdhanTimings timings = day.getTimings();

            if (timings.getFajr() == null || timings.getSunrise() == null || timings.getDhuhr() == null || timings.getAsr() == null ||
                    timings.getMaghrib() == null || timings.getIsha() == null) {
                throw new IllegalStateException("AlAdhan Response is missing prayer timings");
            }

        }
    }

    private void savePrayerTimeMonthly(Year year, Month month, AlAdhanMonthlyResponseDto response) {
        List<AlAdhanData> days = response.getData();
        for(AlAdhanData day: days) {
            AlAdhanTimings timings = day.getTimings();
            PrayerTiming prayerTiming = prayerRepository.findByYearAndMonthAndLocation(year, month, "WINNIPEG")
                    .orElseGet(() -> (PrayerTiming.builder()
                            .locationKey("WINNIPEG")
                            .location("Winnipeg, Manitoba, Canada")
                            .timezone("America/Winnipeg")
                            .createdAt(LocalDateTime.now())
                            .build()));

            prayerTiming.setFajr(parseTime(timings.getFajr()));
            prayerTiming.setSunrise(parseTime(timings.getSunrise()));
            prayerTiming.setDhuhr(parseTime(timings.getDhuhr()));
            prayerTiming.setAsr(parseTime(timings.getAsr()));
            prayerTiming.setMaghrib(parseTime(timings.getMaghrib()));
            prayerTiming.setIsha(parseTime(timings.getIsha()));

            prayerTiming.setFetchedAt(LocalDateTime.now());
            prayerTiming.setUpdatedAt(LocalDateTime.now());
            prayerTiming.setSource(PrayerTimeSource.API);

            prayerRepository.save(prayerTiming);
        }
    }

    private AlAdhanMonthlyResponseDto getPrayerTimeMonthlyFromDatabase(Year year, Month month) {
        LocalDate startDate = year.atMonth(month).atDay(1);
        LocalDate endDate = year.atMonth(month).atEndOfMonth();

        List<PrayerTiming> records = prayerRepository.findByDateBetweenAndLocationKeyOrderByDateAsc(startDate, endDate,"WINNIPEG");

        if(records.isEmpty()){
            throw new PrayerTimingsUnavailableException("Prayer timings not available for " + year + "-" + month);
        }

        List<AlAdhanData> days = new ArrayList<>();

        for(PrayerTiming record: records) {
            AlAdhanTimings timings = new AlAdhanTimings();

            timings.setFajr(formatTime(record.getFajr()));
            timings.setSunrise(formatTime(record.getSunrise()));
            timings.setDhuhr(formatTime(record.getDhuhr()));
            timings.setAsr(formatTime(record.getAsr()));
            timings.setMaghrib(formatTime(record.getMaghrib()));
            timings.setIsha(formatTime(record.getIsha()));

            AlAdhanData day = new AlAdhanData();
            day.setTimings(timings);
            days.add(day);
        }
        return new AlAdhanMonthlyResponseDto(200, "DATABASE_FALLBACK", days);
    }

    private void validateResponse(AlAdhanResponseDto response){
        if(response == null || response.getCode() != 200 || response.getData() == null || response.getData().getTimings() == null){
            throw new IllegalStateException("AlAdhan returned an invalid prayer timings response");
        }
        AlAdhanTimings timings = response.getData().getTimings();

        if (timings.getFajr() == null || timings.getSunrise() == null|| timings.getDhuhr() == null || timings.getAsr() == null
                || timings.getMaghrib() == null || timings.getIsha() == null) {
            throw new IllegalStateException("AlAdhan response is missing prayer timings");
        }
    }

    private void savePrayerTime(LocalDate date, AlAdhanResponseDto response){
        AlAdhanTimings timings = response.getData().getTimings();

        PrayerTiming entity = prayerRepository.findByDateAndLocationKey(date, "WINNIPEG")
                .orElseGet(() -> PrayerTiming.builder()
                        .date(date)
                        .locationKey("WINNIPEG")
                        .location("Winnipeg, Manitoba, Canada")
                        .timezone("America/Winnipeg")
                        .createdAt(LocalDateTime.now())
                        .build());

        entity.setFajr(parseTime(timings.getFajr()));
        entity.setSunrise(parseTime(timings.getSunrise()));
        entity.setDhuhr(parseTime(timings.getDhuhr()));
        entity.setAsr(parseTime(timings.getAsr()));
        entity.setMaghrib(parseTime(timings.getMaghrib()));
        entity.setIsha(parseTime(timings.getIsha()));

        entity.setFetchedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setSource(PrayerTimeSource.API);

        prayerRepository.save(entity);
    }

    private AlAdhanResponseDto getPrayerTimeFromDatabase(LocalDate date){
        PrayerTiming entity = prayerRepository.findByDateAndLocationKey(date, "WINNIPEG")
                .orElseThrow(() -> new PrayerTimingsUnavailableException("Prayer timings unavailable for " + date));

        AlAdhanTimings timings = new AlAdhanTimings();
        timings.setFajr(formatTime(entity.getFajr()));
        timings.setSunrise(formatTime(entity.getSunrise()));
        timings.setDhuhr(formatTime(entity.getDhuhr()));
        timings.setAsr(formatTime(entity.getAsr()));
        timings.setMaghrib(formatTime(entity.getMaghrib()));
        timings.setIsha(formatTime(entity.getIsha()));

        AlAdhanData data = new AlAdhanData();
        data.setTimings(timings);

        return new AlAdhanResponseDto(200, "DATABASE_FALLBACK", data);
    }

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
                        .build(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))))
                .retrieve()
                .body(AlAdhanResponseDto.class);
    }

    private String formatTime(LocalTime time){
        return time == null ? null : time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private LocalTime parseTime(String time){
        if(time== null || time.isBlank())
            return null;
        return LocalTime.parse(time.substring(0, 5));
    }

    @CacheEvict(cacheNames = "prayer_time", allEntries = true)
    public void evictPrayerTime(){}

    @CacheEvict(cacheNames = "prayer_time_date", allEntries = true)
    public void evictPrayerTimeDate(){}

    @CacheEvict(cacheNames = "prayer_time_month", allEntries = true)
    public void evictMonthPrayerTime(){}

}
