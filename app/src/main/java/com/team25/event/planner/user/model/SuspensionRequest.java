package com.team25.event.planner.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuspensionRequest {
    private Long userId;
    private Long reportId;
}
