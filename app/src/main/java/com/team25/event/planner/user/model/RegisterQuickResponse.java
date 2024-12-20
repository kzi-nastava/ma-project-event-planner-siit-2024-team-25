package com.team25.event.planner.user.model;

import lombok.Data;

@Data
public class RegisterQuickResponse {
    private Long userId;
    private String userEmail;
    private String password;
    private Long eventId;
}
