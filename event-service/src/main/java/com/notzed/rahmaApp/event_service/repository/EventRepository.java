package com.notzed.rahmaApp.event_service.repository;

import com.notzed.rahmaApp.event_service.dto.EventDto;
import com.notzed.rahmaApp.event_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOwnerId(Long ownerId);

    Page<EventDto> findEvents(
            @Param("eventAt") String eventAt,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("dateCount") long dateCount,
            Pageable pageable);
}
