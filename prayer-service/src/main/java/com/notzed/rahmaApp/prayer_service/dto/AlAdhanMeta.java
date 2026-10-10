package com.notzed.rahmaApp.prayer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlAdhanMeta {
    private double latitude;
    private double longitude;
    private String timezone;
    private AlAdhanMethod method;
    private String latitudeAdjustmentMethod;
    private String midnightMode;
    private String school;
    private Map<String, Integer> offset;
}
