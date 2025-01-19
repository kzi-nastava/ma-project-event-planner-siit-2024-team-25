package com.team25.event.planner.user.model;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest extends UserRequest {
    private String email;
    private String password;

    public RegisterRequest(String email, String password, String firstName, String lastName, File profilePicture, UserRole userRole, EventOrganizerInfo eventOrganizerFields, OwnerInfo ownerFields) {
        super(firstName, lastName, profilePicture, userRole, eventOrganizerFields, ownerFields);
        this.email = email;
        this.password = password;
    }
}
