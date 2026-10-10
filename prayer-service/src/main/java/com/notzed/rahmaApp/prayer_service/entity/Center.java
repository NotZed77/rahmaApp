package com.notzed.rahmaApp.prayer_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table
public class Center {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long centerId;

    private String name;

    private String address;

    private Double latitude;

    private Double longitude;

    private String timeZone;

}