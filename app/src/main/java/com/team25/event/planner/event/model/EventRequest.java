package com.team25.event.planner.event.model;

import com.team25.event.planner.user.model.Location;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class EventRequest {
    private final Long eventTypeId;
    private final String name;
    private final String description;
    private final PrivacyType privacyType;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Location location;
}
