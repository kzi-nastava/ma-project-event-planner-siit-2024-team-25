package com.team25.event.planner.user.api;

import com.team25.event.planner.user.model.RegisterQuickResponse;
import com.team25.event.planner.user.model.RegisterResponse;
import com.team25.event.planner.user.model.RegularUser;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {
    @POST("/api/auth/register")
    Call<RegisterResponse> register(@Body RequestBody body);

    @POST("/api/auth/register/quick")
    Call<RegisterQuickResponse> registerQuick(@Body RequestBody body);

    @GET("/api/users/{userId}")
    Call<RegularUser> getUser(@Path("userId") Long userId);
}
