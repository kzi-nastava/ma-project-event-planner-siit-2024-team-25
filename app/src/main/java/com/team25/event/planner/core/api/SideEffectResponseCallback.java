package com.team25.event.planner.core.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Use this instead of {@link ResponseCallback} if you want to perform a side-effect
 * when the response is handled (does not matter if it was successful or not).
 */
public class SideEffectResponseCallback<T> extends ResponseCallback<T> {
    public interface SideEffectCallback<T> {
        void handle();
    }

    private final SideEffectCallback<T> sideEffectCallback;

    public SideEffectResponseCallback(
            SuccessCallback<T> successCallback,
            SideEffectCallback<T> sideEffectCallback,
            MutableLiveData<String> serverErrorMessage,
            String logTag
    ) {
        super(successCallback, serverErrorMessage, logTag);
        this.sideEffectCallback = sideEffectCallback;
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        super.onResponse(call, response);
        sideEffectCallback.handle();
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        super.onFailure(call, t);
        sideEffectCallback.handle();
    }
}
