package com.notzed.rahmaApp.prayer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlAdhanResponseDto {
    private int code;
    private String status;
    private AlAdhanData data;
}
