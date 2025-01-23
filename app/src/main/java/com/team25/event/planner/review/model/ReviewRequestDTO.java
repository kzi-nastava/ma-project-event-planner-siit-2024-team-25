package com.team25.event.planner.review.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewRequestDTO {
    private String comment;
    private int rating;
    private ReviewType reviewType;
    private Long purchaseId;
    private Long userId;
}
