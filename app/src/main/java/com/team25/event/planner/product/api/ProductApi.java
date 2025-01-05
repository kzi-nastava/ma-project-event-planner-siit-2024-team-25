package com.team25.event.planner.product.api;

import com.team25.event.planner.core.Page;
import com.team25.event.planner.offering.model.OfferingCard;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ProductApi {
    @GET("/api/products/owner/{ownerId}")
    Call<Page<OfferingCard>> getOwnerProducts(
            @Path("ownerId") Long ownerId,
            @Query("page") int page,
            @QueryMap Map<String, Object> filters
    );
}
