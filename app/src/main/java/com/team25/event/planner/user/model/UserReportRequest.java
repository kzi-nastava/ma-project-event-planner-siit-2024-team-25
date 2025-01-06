package com.team25.event.planner.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserReportRequest {
    private Long reportedUserId;
    private String reportMessage;
}
