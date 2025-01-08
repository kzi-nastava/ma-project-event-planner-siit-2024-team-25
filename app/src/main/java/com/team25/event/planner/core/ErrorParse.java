package com.team25.event.planner.core;

import com.google.gson.Gson;
import com.team25.event.planner.core.model.ApiError;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ErrorParse {
    public static String catchError(Response<?> response){
        try (ResponseBody errorBody = response.errorBody()) {
            if (errorBody != null) {
                Gson gson = new Gson();
                ApiError apiError = gson.fromJson(errorBody.charStream(), ApiError.class);
                return apiError.getMessage();
            } else {
                return "Unknown error occurred";
            }
        } catch (Exception e) {
            return "Error parsing server response";
        }
    }
}
