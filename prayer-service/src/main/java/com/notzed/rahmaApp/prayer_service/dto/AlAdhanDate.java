package com.notzed.rahmaApp.prayer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlAdhanDate {
    private String readable;
    private String timestamp;
    private AlAdhanGregorian gregorian;
    private AlAdhanHijri hijri;
}
