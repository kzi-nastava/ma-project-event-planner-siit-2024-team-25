package com.team25.event.planner.core.model;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {
    private Integer code;
    private String message;
    private Map<String, Object> errors;
}
