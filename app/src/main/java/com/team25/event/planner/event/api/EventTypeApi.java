package com.team25.event.planner.event.api;

import com.team25.event.planner.event.model.EventType;
import com.team25.event.planner.event.model.EventTypeRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EventTypeApi {
    @GET("/api/event-types")
    Call<List<EventType>> getEventTypes();

    @GET("/api/event-types/{id}")
    Call<EventType> getEventType(@Path("id") Long id);

    @POST("/api/event-types")
    Call<EventType> createEventType(@Body EventTypeRequest eventType);

    @PUT("/api/event-types/{id}")
    Call<EventType> updateEventType(@Path("id") Long id, @Body EventTypeRequest eventType);
}
