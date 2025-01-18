package com.team25.event.planner.review.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReviewCard {
    private Long id;
    private String comment;
    private int rating;
    private LocalDateTime createdDate;
    private Long userId;
    private String firstName;
    private String lastName;
}
