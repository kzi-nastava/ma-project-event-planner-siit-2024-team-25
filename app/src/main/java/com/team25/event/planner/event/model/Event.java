package com.team25.event.planner.event.model;

import com.team25.event.planner.user.model.Location;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class Event {
    private Long id;
    private EventTypePreviewDTO eventType;
    private String name;
    private String description;
    private Integer maxParticipants;
    private PrivacyType privacyType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Location location;
}
