package com.team25.event.planner.event.api;

import com.team25.event.planner.event.model.EventType;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EventTypeApi {
    @GET("/api/event-types")
    Call<List<EventType>> getEventTypes();
}
