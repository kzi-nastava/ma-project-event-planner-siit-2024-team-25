package com.team25.event.planner.core;



import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.team25.event.planner.BuildConfig;
import com.team25.event.planner.event.api.EventApi;

import java.time.LocalDateTime;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectiongParams {
    public static final String BASE_URL = BuildConfig.BASE_URL;


    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static EventApi eventApi = retrofit.create(EventApi.class);

}
