package com.notzed.rahmaApp.event_service.service;

import com.notzed.rahmaApp.event_service.auth.UserContextHolder;
import com.notzed.rahmaApp.event_service.dto.EventDto;

import com.notzed.rahmaApp.event_service.entity.Event;
import com.notzed.rahmaApp.event_service.repository.EventRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;



@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public EventDto getEventById(Long eventId){
        log.info("Getting the event with ID: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: {}"+ eventId));

        return modelMapper.map(event, EventDto.class);
    }

    public List<EventDto> getAllEventsOfUser(Long userId) {
        List<Event> events = eventRepository.findByOwnerId(userId);
        log.info("Getting all the events");
        return events.stream()
                .map((element) -> modelMapper.map(element, EventDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public EventDto createNewEvent(EventDto eventDto) {
        log.info("Creating a new event with name: {}", eventDto.getEventName());
        Long userId = UserContextHolder.getCurrentUserId();

        Event event = modelMapper.map(eventDto, Event.class);

        event.setActive(false);
        event.setOwnerId(userId);

        event = eventRepository.save(event);
        log.info("Created a new event with ID: {}", eventDto.getEventId());
        return modelMapper.map(event, EventDto.class);
    }

    @Transactional
    public EventDto updateEventById(Long eventId, EventDto eventDto) {
        log.info("Updating the event with ID: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: {}"+ eventId));

        Long userId = UserContextHolder.getCurrentUserId();

        if(!userId.equals(event.getOwnerId())){
            throw new RuntimeException("This user does not own this event with ID: " + eventId);
        }

        modelMapper.map(eventDto, event);
        event.setEventId(eventId);
        event = eventRepository.save(event);
        return modelMapper.map(event, EventDto.class);
    }

    @Transactional
    public void deleteEventById(Long eventId) {
        Long user = UserContextHolder.getCurrentUserId();
        log.info("Deleting the event with ID: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: {}"+ eventId));

        if(!user.equals(event.getOwnerId())){
            throw new RuntimeException("This user does not own this event with ID: " + eventId);
        }

        eventRepository.deleteById(eventId);
    }


    @Transactional
    public void activateEvent(Long eventId) {
        Long user = UserContextHolder.getCurrentUserId();
        log.info("Activating the event with ID: {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: {}"+ eventId));
        if(event.getActive().equals(true)){
            throw new RuntimeException("Event is already active");
        }

        if(!user.equals(event.getOwnerId())){
            throw new RuntimeException("This user does not own this event with ID: " + eventId);
        }
        event.setActive(true);
    }


}
