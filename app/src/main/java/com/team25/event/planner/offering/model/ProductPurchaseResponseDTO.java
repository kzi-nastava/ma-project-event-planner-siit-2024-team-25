package com.team25.event.planner.offering.model;

import lombok.Data;

@Data
public class ProductPurchaseResponseDTO {
    private Long id;
    private Double price;
    private Long offeringCategoryId;
    private Long eventId;
    private Long productId;
}
