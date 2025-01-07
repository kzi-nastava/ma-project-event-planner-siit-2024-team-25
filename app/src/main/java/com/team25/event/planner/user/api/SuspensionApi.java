package com.team25.event.planner.user.api;

import com.team25.event.planner.communication.model.NotificationRequestDTO;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.user.model.SuspensionRequest;
import com.team25.event.planner.user.model.SuspensionResponse;
import com.team25.event.planner.user.model.UserReportResponse;
import com.team25.event.planner.user.model.UserReportUpdateRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface SuspensionApi {

    @GET("/api/users/reports")
    Call<Page<UserReportResponse>> getAllReports(@Query("page") int page, @Query("viewed") boolean isViewed);

    @PUT("/api/users/report")
    Call<UserReportResponse> updateReport(@Body UserReportUpdateRequest request);
    @POST("/api/users/suspend")
    Call<SuspensionResponse> suspendUser(@Body SuspensionRequest request);
}
