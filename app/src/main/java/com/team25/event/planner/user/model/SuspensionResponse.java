package com.team25.event.planner.user.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SuspensionResponse {
    private Long id;
    private Long suspendedUserId;
    private String suspendedUserFirstName;
    private String suspendedUserLastName;
    private LocalDateTime expirationTime;
}
