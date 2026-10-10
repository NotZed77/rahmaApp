package com.notzed.rahmaApp.prayer_service.controller;


import com.notzed.rahmaApp.prayer_service.dto.AlAdhanMonthlyResponseDto;
import com.notzed.rahmaApp.prayer_service.dto.AlAdhanResponseDto;
import com.notzed.rahmaApp.prayer_service.service.PrayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timings")
public class PrayerController {

    private final PrayerService prayerService;

    @GetMapping("/today")
    public ResponseEntity<AlAdhanResponseDto> getPrayerTimeToday(){
        return new ResponseEntity<>(prayerService.getPrayerTimeForToday(), HttpStatus.OK);
    }

    @GetMapping("/{date}")
    public ResponseEntity<AlAdhanResponseDto> getPrayerTimeForDate(@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date){
        return new ResponseEntity<>(prayerService.getPrayerTimeForDate(date), HttpStatus.OK);
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<AlAdhanMonthlyResponseDto> getPrayerTimesForMonth(@PathVariable int year, @PathVariable int month){
        return new ResponseEntity<>(prayerService.getPrayerTimeForMonth(Year.of(year), Month.of(month)), HttpStatus.OK);
    }

    @DeleteMapping("/cache")
    public ResponseEntity<Void> clearCache(@RequestParam(required = false) LocalDate date){
        prayerService.evictPrayerTime();
        if(date != null){
            prayerService.evictPrayerTimeDate();
            prayerService.evictMonthPrayerTime();
        }
        return ResponseEntity.noContent().build();
    }
}
