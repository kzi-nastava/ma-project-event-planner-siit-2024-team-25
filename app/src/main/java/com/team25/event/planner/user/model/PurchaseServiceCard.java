package com.team25.event.planner.user.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class PurchaseServiceCard {
    private Long id;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private Double price;
    private String eventName;
    private String serviceName;
}
