package com.team25.event.planner.event.model;

import com.team25.event.planner.offering.model.OfferingCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetItem {
    private Long id;
    private Double budget;
    private OfferingCategory offeringCategory;
    private Event event;
}
