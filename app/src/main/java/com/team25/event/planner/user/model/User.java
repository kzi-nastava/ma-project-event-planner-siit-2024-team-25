package com.team25.event.planner.user.model;

import lombok.Data;

@Data
public class User {
    private final String firstName;
    private final String lastName;
    private final String email;

    public String getFullName(){
        return firstName + " " + lastName;
    }
}
