package com.team25.event.planner.event.model;

import androidx.test.espresso.core.internal.deps.dagger.internal.DaggerGenerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetItemRequestDTO {
    private Double budget;
    private Long offeringCategoryId;
    private Long eventId;
}
