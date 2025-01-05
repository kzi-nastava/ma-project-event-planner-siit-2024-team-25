package com.team25.event.planner.communication.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification implements Serializable {
    private Long id;
    private String title;
    private String message;
    private Boolean isViewed;
    private Long entityId;
    private NotificationCategory notificationCategory;
    private LocalDateTime createdDate;
}
