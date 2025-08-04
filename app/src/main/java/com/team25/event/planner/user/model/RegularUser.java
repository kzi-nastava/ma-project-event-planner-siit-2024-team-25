package com.team25.event.planner.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegularUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String profilePictureUrl;
    private UserRole userRole;
}
