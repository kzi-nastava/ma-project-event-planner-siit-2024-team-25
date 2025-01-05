package com.team25.event.planner.user.api;

import com.team25.event.planner.user.model.UserReportRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserReportApi {

    @POST("/api/users/report")
    Call<ResponseBody> reportUser(@Body UserReportRequest userReportRequest);
}
