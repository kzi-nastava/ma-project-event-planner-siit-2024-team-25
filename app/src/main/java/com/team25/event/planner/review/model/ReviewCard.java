package com.team25.event.planner.review.model;

import java.util.Date;

import lombok.Data;

@Data
public class ReviewCard {
    private Long id;
    private String comment;
    private int rating;
    private ReviewType reviewType;
    private ReviewStatus reviewStatus;
    private Date createdDate;
    private Long purchaseId;
}
