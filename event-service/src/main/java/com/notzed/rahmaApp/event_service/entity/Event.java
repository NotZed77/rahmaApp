package com.notzed.rahmaApp.event_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private LocalDateTime eventTime;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private EventContactInfo contactInfo;

}
