package com.team25.event.planner.user.model;

import lombok.Data;

@Data
public class SuspensionRequest {
    private Long userId;
    private Long reportId;
}
