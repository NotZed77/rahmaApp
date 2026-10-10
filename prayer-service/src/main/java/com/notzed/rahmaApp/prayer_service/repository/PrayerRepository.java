package com.notzed.rahmaApp.prayer_service.repository;

import com.notzed.rahmaApp.prayer_service.entity.Prayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrayerRepository extends JpaRepository<Prayer, Long> {
}
