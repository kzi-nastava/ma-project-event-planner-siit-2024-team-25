package com.team25.event.planner.offering.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmittedOfferingCategory {
    private Long offeringId;
    private String offeringName;
    private Long categoryId;
    private String categoryName;
    private String description;
    private OfferingCategoryType status;
}
