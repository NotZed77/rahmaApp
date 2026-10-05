package com.notzed.rahmaApp.event_service.controller;

import com.notzed.rahmaApp.event_service.dto.EventDto;
import com.notzed.rahmaApp.event_service.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {

    private final EventService eventService;

    @PostMapping("/createEvent")
    public ResponseEntity<EventDto> createNewEvent(@RequestBody EventDto eventDto){
        log.info("Attempting to create a new event with name: {}", eventDto.getEventName());
        EventDto event = eventService.createNewEvent(eventDto);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEventById(@PathVariable Long eventId, @RequestBody EventDto eventDto){
        EventDto event = eventService.updateEventById(eventId, eventDto);
        return ResponseEntity.ok(eventDto);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEventById(@PathVariable Long eventId){
        eventService.deleteEventById(eventId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{eventId}/activate")
    public ResponseEntity<Void> activateHotelById(@PathVariable Long eventId){
        eventService.activateEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
