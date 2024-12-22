package com.team25.event.planner.core.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.team25.event.planner.core.model.ApiError;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResponseCallback<T> implements Callback<T> {
    public interface SuccessCallback<T> {
        void handle(T responseBody);
    }

    private final SuccessCallback<T> successCallback;

    private final MutableLiveData<String> serverErrorMessage;
    private final String logTag;

    public ResponseCallback(
            SuccessCallback<T> successCallback,
            MutableLiveData<String> serverErrorMessage,
            String logTag
    ) {
        this.successCallback = successCallback;
        this.serverErrorMessage = serverErrorMessage;
        this.logTag = logTag;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (response.isSuccessful() && (response.body() != null || response.code() == 204)) {
            successCallback.handle(response.body());
        } else {
            try (ResponseBody errorBody = response.errorBody()) {
                if (errorBody != null) {
                    Gson gson = new Gson();
                    ApiError apiError = gson.fromJson(errorBody.charStream(), ApiError.class);
                    serverErrorMessage.postValue(apiError.getMessage());
                    Log.e(logTag, "Error: " + apiError.getMessage());
                } else {
                    serverErrorMessage.postValue("Unknown error occurred");
                    Log.e(logTag, "Error response with empty body");
                }
            } catch (Exception e) {
                serverErrorMessage.postValue("Error parsing server response");
                Log.e(logTag, "Error parsing response: " + e.getMessage());
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        Log.e(logTag, "Network error on event type fetch: " + t.getMessage());
        serverErrorMessage.postValue("Network error");
    }
}
