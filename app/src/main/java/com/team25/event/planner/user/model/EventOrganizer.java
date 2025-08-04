package com.team25.event.planner.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventOrganizer extends RegularUser {
    private Location livingAddress;
    private String phoneNumber;
}
