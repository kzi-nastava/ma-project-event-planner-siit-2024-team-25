package com.team25.event.planner.product_service.dto;

import com.team25.event.planner.core.model.Money;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class ServicePurchaseResponseDTO {
    private Long id;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private Money price;
    private Long offeringCategoryId;
    private Long eventId;
    private Long serviceId;
}
