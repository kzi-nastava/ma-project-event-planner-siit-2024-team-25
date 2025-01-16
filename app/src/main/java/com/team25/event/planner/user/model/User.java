package com.team25.event.planner.user.model;

import java.time.Instant;

import lombok.Data;

@Data
public class User {
    private final Long id;
    private final String email;
    private final String name;
    private final UserRole userRole;
    private final Instant suspensionEndDateTime;
}
