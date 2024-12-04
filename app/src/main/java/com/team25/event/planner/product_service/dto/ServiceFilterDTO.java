package com.team25.event.planner.product_service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ServiceFilterDTO {
    private String name;
    private Long eventTypeId;
    private Long offeringCategoryId;
    private Double price;
    private Boolean available;
    private Long ownerId;
}

