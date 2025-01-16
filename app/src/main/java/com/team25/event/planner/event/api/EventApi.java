package com.team25.event.planner.event.api;

import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.model.Activity;
import com.team25.event.planner.event.model.Event;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.event.model.EventRequest;
import com.team25.event.planner.event.model.FavoriteEventRequest;
import com.team25.event.planner.event.model.Invitation;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface EventApi {
    @GET("/api/events/{eventId}")
    Call<Event> getEvent(
            @Path("eventId") Long eventId,
            @Query("invitationCode") String invitationCode
    );

    @GET("/api/events/top")
    Call<Page<EventCard>> getTopEvents();

    @GET("/api/events/all")
    Call<Page<EventCard>> getAllEvents(
            @Query("page") int page,
            @QueryMap Map<String, String> filter
    );

    @GET("/api/events/")
    Call<Page<EventCard>> getOrganizerEvents(@Query("page") int page);

    @POST("/api/events/{eventId}/send-invitations")
    Call<Void> sendInvitations(
            @Path("eventId") Long eventId,
            @Body List<Invitation> requestDTO
    );

    @POST("/api/events")
    Call<Event> createEvent(@Body EventRequest eventRequest);

    @GET("/api/events/{eventId}/agenda")
    Call<List<Activity>> getAgenda(@Path("eventId") Long eventId);

    @POST("/api/events/{eventId}/agenda")
    Call<Activity> addActivity(@Path("eventId") Long eventId, @Body Activity activity);

    @DELETE("/api/events/{eventId}/agenda/{activityId}")
    Call<Void> removeActivity(@Path("eventId") Long eventId, @Path("activityId") Long activityId);

    @GET("/api/events/{eventId}")
    Call<Event> getEvent(@Path("eventId") Long eventId);

    @GET("/api/users/{userId}/favorite-events")
    Call<List<EventCard>> getFavoriteEvents(@Path("userId") Long userId);

    @POST("/api/users/{userId}/favorite-events")
    Call<EventCard> addToFavorites(@Path("userId") Long userId, @Body FavoriteEventRequest favRequest);

    @DELETE("/api/users/{userId}/favorite-events/{favId}")
    Call<Void> removeFromFavorites(@Path("userId") Long userId, @Path("favId") Long eventId);
}
