package com.team25.event.planner.service.dto;

import com.team25.event.planner.service.enums.ReservationType;

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
    private boolean visible;
    private boolean available;
    private String specifics;
    private ReservationType reservationType;
    private int duration;
    private int reservationDeadline;
    private int cancellationDeadline;
    private int minimumArrangement;
    private int maximumArrangement;
    private List<Long> eventTypesIDs;
    private Long offeringCategoryID;
    private String offeringCategoryName;
    private Long ownerId;
}
