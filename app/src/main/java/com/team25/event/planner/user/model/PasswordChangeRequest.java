package com.team25.event.planner.user.model;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private final String oldPassword;
    private final String newPassword;
}
