package com.team25.event.planner.user.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CalendarEvent {
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final EventType eventType;

    public enum EventType {
        EVENT,
        MY_EVENT,
        RESERVATION
    }
}
