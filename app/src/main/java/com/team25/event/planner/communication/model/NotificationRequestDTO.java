package com.team25.event.planner.communication.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequestDTO {
    private Long id;
    private boolean isViewed;
}
