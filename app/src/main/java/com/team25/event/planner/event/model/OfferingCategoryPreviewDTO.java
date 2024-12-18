package com.team25.event.planner.event.model;

import androidx.annotation.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OfferingCategoryPreviewDTO {
    private final Long id;
    private final String name;

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
