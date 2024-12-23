package com.team25.event.planner.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import retrofit2.http.GET;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Long id;
    private String name;

}
