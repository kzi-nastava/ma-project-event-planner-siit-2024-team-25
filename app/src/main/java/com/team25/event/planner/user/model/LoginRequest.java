package com.team25.event.planner.user.model;

import lombok.Data;

@Data
public class LoginRequest {
    private final String email;
    private final String password;
}
