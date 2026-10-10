package com.notzed.rahmaApp.prayer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlAdhanMonthlyResponseDto {

    private int code;
    private String status;
    private List<AlAdhanData> data;
}