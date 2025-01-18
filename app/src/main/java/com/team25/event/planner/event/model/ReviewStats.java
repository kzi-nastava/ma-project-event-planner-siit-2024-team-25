package com.team25.event.planner.event.model;

import java.util.Map;

import lombok.Data;

@Data
public class ReviewStats {
    private Integer reviewCount;
    private Double averageRating;
    private Map<Integer, Integer> reviewCounts;
}
