package com.team25.event.planner.user.model;

import java.io.File;

import lombok.Value;

@Value
public class RegisterRequest {
    String email;
    String password;
    String firstName;
    String lastName;
    File profilePicture;
    UserRole userRole;
    EventOrganizerInfo eventOrganizerFields;
    OwnerInfo ownerFields;
}
