package com.team25.event.planner.product.model;

import com.team25.event.planner.offering.model.OfferingCategory;

import java.util.List;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private String name;
    private String description;
    private double price;
    private double discount;
    private List<String> images;
    private boolean isVisible;
    private boolean isAvailable;
    private List<EventType> eventTypes;
    private OfferingCategory offeringCategory;
    private OwnerPreview ownerInfo;
    private boolean isFavorite;
}
