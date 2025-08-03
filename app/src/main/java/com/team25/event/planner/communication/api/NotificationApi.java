package com.team25.event.planner.communication.api;

import com.team25.event.planner.communication.model.Notification;
import com.team25.event.planner.communication.model.NotificationRequestDTO;
import com.team25.event.planner.core.Page;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotificationApi {
    @GET("api/notifications/")
    Call<Page<Notification>> getMyNotifications(@Query("page") int page);

    @PUT("api/notifications/")
    Call<Notification> toggleViewed(@Body NotificationRequestDTO notification);
}
