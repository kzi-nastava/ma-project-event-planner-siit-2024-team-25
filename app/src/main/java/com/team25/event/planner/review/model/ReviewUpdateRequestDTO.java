package com.team25.event.planner.review.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewUpdateRequestDTO {
    private ReviewStatus reviewStatus;
}
