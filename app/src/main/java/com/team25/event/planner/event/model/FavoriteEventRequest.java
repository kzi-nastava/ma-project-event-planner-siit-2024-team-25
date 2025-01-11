package com.team25.event.planner.event.model;

import lombok.Data;

@Data
public class FavoriteEventRequest {
    private final Long userId;
    private final Long eventId;
}
