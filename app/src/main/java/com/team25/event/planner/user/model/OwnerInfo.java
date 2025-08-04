package com.team25.event.planner.user.model;

import lombok.Value;

@Value
public class OwnerInfo {
    String companyName;
    Location companyAddress;
    String contactPhone;
    String description;
}
