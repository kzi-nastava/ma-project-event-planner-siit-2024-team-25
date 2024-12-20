package com.team25.event.planner.event.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Activity {
    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String location;
}
