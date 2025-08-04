package com.team25.event.planner.user.model;

import lombok.Value;

@Value
public class RegisterResponse {
    String email;
    String fullName;
    UserRole userRole;
}
