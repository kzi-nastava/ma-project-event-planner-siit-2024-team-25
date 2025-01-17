package com.team25.event.planner.communication.api;

import com.team25.event.planner.communication.model.ChatMessage;
import com.team25.event.planner.communication.model.ChatRoom;
import com.team25.event.planner.core.Page;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatRoomApi {
    @GET("/api/chats/{senderId}")
    Call<Page<ChatRoom>> getChats(
            @Path("senderId") Long senderId,
            @Query("page") int page,
            @Query("size") int size,
            @Query("sortBy") String sortBy,
            @Query("sortDirection") String sortDirection);
}
