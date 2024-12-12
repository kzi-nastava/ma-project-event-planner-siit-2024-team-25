package com.team25.event.planner.offering.Api;

import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.model.SubmittedOfferingCategory;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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
    @GET("api/offerings/submitted")
    Call<List<SubmittedOfferingCategory>> getSubmittedCategories();
    @PUT("api/offerings/{id}/updateCategory")
    Call<ResponseBody> updateOfferingsCategory(@Path("id") Long id,
                                               @Query("categoryId") Long categoryId,
                                               @Query("updateCategoryId") Long updateCategoryId);
}
