package com.team25.event.planner.event.model;

import com.team25.event.planner.core.model.Money;

import lombok.Data;

@Data
public class PurchaseResponseDTO {
    private Long id;
    private OfferingCategoryPreviewDTO offering;
    private Money price;
    private EventCard event;
}
