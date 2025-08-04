package com.team25.event.planner.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BudgetItemResponseDTO {
    private Long id;
    private Double budget;
    private Long offeringCategoryId;
    private Long eventId;
}
