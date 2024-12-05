package com.team25.event.planner.core;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.team25.event.planner.BuildConfig;
import com.team25.event.planner.event.api.EventApi;
import com.team25.event.planner.user.api.UserApi;
import com.team25.event.planner.offering.Api.OfferingApi;

import java.time.LocalDateTime;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionParams {
    public static final String BASE_URL = BuildConfig.BASE_URL;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static EventApi eventApi = retrofit.create(EventApi.class);

    public static UserApi userApi = retrofit.create(UserApi.class);

    public static OfferingApi offeringApi = retrofit.create(OfferingApi.class);
}
