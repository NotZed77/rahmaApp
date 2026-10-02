package com.notzed.rahmaApp.event_service.repository;

import com.notzed.rahmaApp.event_service.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOwnerId(Long ownerId);

}
