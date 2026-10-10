package com.notzed.rahmaApp.prayer_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlAdhanGregorian {

    private String date;
    private String format;
    private String day;
    private AlAdhanWeekday weekday;
    private AlAdhanMonth month;
    private String year;
}
