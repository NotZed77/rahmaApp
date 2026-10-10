package com.notzed.rahmaApp.prayer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlAdhanTimings {
    private String Fajr;
    private String Sunrise;
    private String Dhuhr;
    private String Asr;
    private String Maghrib;
    private String Isha;
}
