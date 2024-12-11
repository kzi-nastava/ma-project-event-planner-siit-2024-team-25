package com.team25.event.planner.core;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private static final String HEADER_NAME = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final String jwt;
    private final LogoutHandler logoutHandler;

    public AuthInterceptor(String jwt, LogoutHandler logoutHandler) {
        this.jwt = jwt;
        this.logoutHandler = logoutHandler;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (jwt == null) {
            return chain.proceed(chain.request());
        }

        Request request = chain.request().newBuilder()
                .header(HEADER_NAME, PREFIX + jwt)
                .build();

        try (Response response = chain.proceed(request)) {
            if (response.code() == 401) {
                logoutHandler.handleLogout();
            }
            return response;
        }
    }

    public interface LogoutHandler {
        void handleLogout();
    }
}
