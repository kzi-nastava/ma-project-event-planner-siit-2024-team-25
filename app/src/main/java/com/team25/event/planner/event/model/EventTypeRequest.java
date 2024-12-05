package com.team25.event.planner.event.model;

import java.util.List;

import lombok.Data;

@Data
public class EventTypeRequest {
    private final String name;
    private final String description;
    private final Boolean isActive;
    private final List<Long> categories;
}
