package com.notzed.rahmaApp.event_service.controller;

import com.notzed.rahmaApp.event_service.dto.EventDto;
import com.notzed.rahmaApp.event_service.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<EventDto>> getAllEvents(@PathVariable Long userId){
        return ResponseEntity.ok(eventService.getAllEventsOfUser(userId));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long eventId){
        EventDto eventDto = eventService.getEventById(eventId);
        return new ResponseEntity<>(eventDto, HttpStatus.FOUND);
    }

}
