package com.team25.event.planner.event.model;

import androidx.annotation.NonNull;

import lombok.Data;

@Data
public class EventTypePreviewDTO {
    public Long id;
    private String name;

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
