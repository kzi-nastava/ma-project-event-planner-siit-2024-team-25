package com.team25.event.planner.product_service.dto;

import com.team25.event.planner.product_service.enums.ReservationType;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceCreateRequestDTO {

    private String name;
    private String description;
    private double price;
    private double discount;
    private List<String> images;
    private boolean isVisible;
    private boolean isAvailable;
    private String specifics;
    private ReservationType reservationType;
    private int duration;
    private int reservationDeadline;
    private int cancellationDeadline;
    private List<Long> eventTypesIDs;
    private Long offeringCategoryID;
    private Long ownerId;
}
