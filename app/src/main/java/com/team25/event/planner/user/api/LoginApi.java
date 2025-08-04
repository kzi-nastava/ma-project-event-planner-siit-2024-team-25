package com.team25.event.planner.user.api;

import com.team25.event.planner.user.model.LoginRequest;
import com.team25.event.planner.user.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);
}
