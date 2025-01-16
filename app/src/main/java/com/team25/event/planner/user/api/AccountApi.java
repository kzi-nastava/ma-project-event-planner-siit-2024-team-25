package com.team25.event.planner.user.api;

import com.team25.event.planner.user.model.PasswordChangeRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface AccountApi {
    @PUT("/api/auth/password-reset")
    Call<Void> changePassword(@Body PasswordChangeRequest passwordChangeRequest);
}
