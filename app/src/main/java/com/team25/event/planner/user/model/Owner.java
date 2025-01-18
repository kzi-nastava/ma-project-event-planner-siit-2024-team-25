package com.team25.event.planner.user.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Owner extends RegularUser {
    private String companyName;
    private Location companyAddress;
    private String contactPhone;
    private String description;
    private List<String> companyPictures;
}
