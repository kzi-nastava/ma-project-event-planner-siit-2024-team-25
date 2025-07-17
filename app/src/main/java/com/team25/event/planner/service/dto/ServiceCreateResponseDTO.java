package com.team25.event.planner.service.dto;

import com.team25.event.planner.event.model.EventTypePreviewDTO;
import com.team25.event.planner.event.model.OfferingCategoryPreviewDTO;
import com.team25.event.planner.product.model.OwnerPreview;
import com.team25.event.planner.service.enums.ProductServiceType;
import com.team25.event.planner.service.enums.ReservationType;

import java.util.List;

import lombok.Data;

@Data
public class ServiceCreateResponseDTO {
    private Long Id;
    private String name;
    private String description;
    private double price;
    private double discount;
    private List<String> images;
    private boolean isVisible;
    private boolean isAvailable;
    private String specifics;
    private ProductServiceType status;
    private ReservationType reservationType;
    private int duration;
    private int reservationDeadline;
    private int cancellationDeadline;
    private int minimumArrangement;
    private int maximumArrangement;
    private List<EventTypePreviewDTO> eventTypes;
    private OfferingCategoryPreviewDTO offeringCategory;
    private OwnerPreview owner;
    private Boolean isFavorite;
}
