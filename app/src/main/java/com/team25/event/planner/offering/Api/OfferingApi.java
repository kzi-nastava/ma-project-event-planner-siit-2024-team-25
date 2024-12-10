package com.team25.event.planner.offering.Api;

import com.team25.event.planner.core.Page;
import com.team25.event.planner.offering.model.OfferingCard;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface OfferingApi {
    @GET("/api/offerings/top")
    Call<Page<OfferingCard>> getTopOfferings(
            @Query("country") String country,
            @Query("city") String city
    );

    @GET("/api/offerings/")
    Call<Page<OfferingCard>> getAllOfferings(
            @Query("page") int page,
            @QueryMap Map<String, Object> filter
    );

    @GET("/api/products/all")
    Call<Page<OfferingCard>> getAllProducts(
            @Query("page") int page,
            @QueryMap Map<String, Object> filter
    );

    @GET("/api/services/all")
    Call<Page<OfferingCard>> getAllServices(
            @Query("page") int page,
            @QueryMap Map<String, Object> filter
    );

}
