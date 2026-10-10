package com.notzed.rahmaApp.prayer_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CenterDto {
    private Long centerId;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String timeZone;

}
