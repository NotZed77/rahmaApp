package com.notzed.rahmaApp.prayer_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlAdhanMethodParams {

    @JsonProperty("Fajr")
    private int fajr;

    @JsonProperty("Isha")
    private int isha;
}