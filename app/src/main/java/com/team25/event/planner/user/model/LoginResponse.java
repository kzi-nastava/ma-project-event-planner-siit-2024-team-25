package com.team25.event.planner.user.model;

import lombok.Data;

@Data
public class LoginResponse {
    private final Long userId;
    private final String email;
    private final String fullName;
    private final UserRole role;
    private final String jwt;
}
