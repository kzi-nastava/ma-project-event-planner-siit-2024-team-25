package com.team25.event.planner.communication.api;

import com.team25.event.planner.communication.model.ChatMessage;
import com.team25.event.planner.communication.model.Notification;
import com.team25.event.planner.core.Page;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatApi {
    @GET("/api/messages/{senderId}/{receiverId}")
    Call<Page<ChatMessage>> getChatMessages(@Path("senderId") Long senderId,
            @Path("receiverId") Long receiverId,
            @Query("page") int page);
}
