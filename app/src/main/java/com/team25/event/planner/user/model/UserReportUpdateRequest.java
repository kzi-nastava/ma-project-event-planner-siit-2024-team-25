package com.team25.event.planner.user.model;

import lombok.Data;

@Data
public class UserReportUpdateRequest {
    private Long reportId;
    private Boolean isViewed;
}
