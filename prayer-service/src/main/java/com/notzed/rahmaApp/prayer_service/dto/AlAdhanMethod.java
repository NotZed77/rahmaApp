package com.notzed.rahmaApp.prayer_service.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlAdhanMethod {

    private int id;
    private String name;
    private AlAdhanMethodParams params;
}