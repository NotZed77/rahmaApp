package com.notzed.rahmaApp.event_service.repository;

import com.notzed.rahmaApp.event_service.dto.EventDto;
import com.notzed.rahmaApp.event_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByOwnerId(Long ownerId);

    @Query(
            value = """
        SELECT new com.notzed.rahmaApp.event_service.dto.EventDto(
            e.ownerId,
            e.eventId,
            e.eventName,
            e.eventTime,
            e.active,
            e.contactInfo
        )
        FROM Event e
        WHERE e.eventTime >= :startTime
          AND e.eventTime <= :endTime
        """,
            countQuery = """
        SELECT COUNT(e)
        FROM Event e
        WHERE e.eventTime >= :startTime
          AND e.eventTime <= :endTime
        """
    )
    Page<EventDto> findEvents(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable
    );
}
