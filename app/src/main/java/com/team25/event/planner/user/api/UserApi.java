package com.team25.event.planner.user.api;

import com.team25.event.planner.user.model.RegisterQuickRequest;
import com.team25.event.planner.user.model.RegisterQuickResponse;
import com.team25.event.planner.user.model.RegisterResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {
    @POST("/api/auth/register")
    Call<RegisterResponse> register(@Body RequestBody body);

    @POST("/api/auth/register/quick")
    Call<RegisterQuickResponse> registerQuick(@Body RequestBody body);
}
