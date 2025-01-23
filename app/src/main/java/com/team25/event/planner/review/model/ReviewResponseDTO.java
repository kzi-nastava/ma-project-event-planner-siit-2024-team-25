package com.team25.event.planner.review.model;

import com.team25.event.planner.user.dto.UserResponseDTO;

import java.util.Date;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private Long id;
    private String comment;
    private int rating;
    private ReviewType reviewType;
    private ReviewStatus reviewStatus;
    private Date createdDate;
    private Long purchaseId;
    private UserResponseDTO user;
    private String eventOfferingName;
}
