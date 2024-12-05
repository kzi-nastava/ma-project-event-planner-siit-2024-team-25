package com.team25.event.planner.event.model;

import java.util.List;

import lombok.Data;

@Data
public class EventType {
    private final Long id;
    private final String name;
    private final String description;
    private final Boolean isActive;
    private List<OfferingCategoryPreviewDTO> categories;
}
