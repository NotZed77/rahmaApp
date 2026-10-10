package com.notzed.rahmaApp.prayer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlAdhanData {
    private AlAdhanTimings timings;
    private AlAdhanDate date;
    private AlAdhanMeta meta;
}
