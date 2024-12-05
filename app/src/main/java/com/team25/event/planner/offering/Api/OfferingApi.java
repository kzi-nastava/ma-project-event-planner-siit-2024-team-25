package com.team25.event.planner.offering.Api;

import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.offering.model.OfferingCard;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OfferingApi {
    @GET("/api/offerings/top")
    Call<Page<OfferingCard>> getTopOfferings(
            @Query("country") String country,
            @Query("city") String city
    );

    @GET("/api/offerings/")
    Call<Page<OfferingCard>> getAllOfferings(
            @Query("page") int page
    );
}
