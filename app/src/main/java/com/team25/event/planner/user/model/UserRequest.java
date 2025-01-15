package com.team25.event.planner.user.model;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String firstName;
    private String lastName;
    private File profilePicture;
    private UserRole userRole;
    private EventOrganizerInfo eventOrganizerFields;
    private OwnerInfo ownerFields;
}
