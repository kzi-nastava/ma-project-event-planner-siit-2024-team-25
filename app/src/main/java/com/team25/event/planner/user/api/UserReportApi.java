package com.team25.event.planner.user.api;

import com.team25.event.planner.user.model.UserReportRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserReportApi {

    @POST("/api/users/{reportedUserId}/report")
    Call<ResponseBody> reportUser(@Path("reportedUserId") Long reportedUserId, @Body UserReportRequest userReportRequest);
}
