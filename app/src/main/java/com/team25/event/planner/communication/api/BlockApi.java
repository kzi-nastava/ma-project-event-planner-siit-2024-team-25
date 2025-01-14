package com.team25.event.planner.communication.api;

import com.team25.event.planner.communication.model.BlockUserRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BlockApi {

    @POST("/api/users/block")
    Call<Boolean> blockUser(@Body BlockUserRequestDTO requestDTO);

    @GET("/api/users/block/{blockedUserId}")
    Call<Boolean> isBlocked(@Path("blockedUserId") Long blockedUserId);
}
