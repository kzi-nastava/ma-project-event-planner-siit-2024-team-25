package com.team25.event.planner.event.api;

import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.event.model.EventFilterDTO;
import com.team25.event.planner.event.model.Invitation;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface EventApi {
    @GET("/api/events/top")
    Call<Page<EventCard>> getTopEvents(
            @Query("country") String country,
            @Query("city") String city
    );

    @GET("/api/events/all")
    Call<Page<EventCard>> getAllEvents(
            @Query("page") int page,
            @QueryMap Map<String, String> filter
    );

    @POST("/api/events/{eventId}/send-invitations")
    Call<Void> sendInvitations(
            @Path("eventId") Long eventId,
            @Body List<Invitation> requestDTO
    );
}
