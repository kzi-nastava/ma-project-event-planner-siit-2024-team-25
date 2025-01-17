package com.team25.event.planner.user.dto;

import com.team25.event.planner.user.model.UserRole;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private UserRole userRole;
    private String profilePictureUrl;
}
