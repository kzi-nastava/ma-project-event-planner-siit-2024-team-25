package com.team25.event.planner.user.model;

import java.time.Instant;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserReportResponse {
    private Long id;
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String reportMessage;
    private Long reportedUserId;
    private String reportedUserFirstName;
    private String reportedUserLastName;
    private Boolean isViewed;
    private LocalDateTime createdDate;
}
