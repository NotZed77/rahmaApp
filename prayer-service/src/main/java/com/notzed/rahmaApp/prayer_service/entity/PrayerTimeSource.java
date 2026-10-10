package com.notzed.rahmaApp.prayer_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


public enum PrayerTimeSource {
    API,
    MANUAL
}