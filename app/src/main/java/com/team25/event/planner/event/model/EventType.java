package com.team25.event.planner.event.model;

import androidx.annotation.NonNull;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class EventType {
    private final Long id;
    private final String name;
    private final String description;
    private final Boolean isActive;
    private List<OfferingCategoryPreviewDTO> categories;

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
