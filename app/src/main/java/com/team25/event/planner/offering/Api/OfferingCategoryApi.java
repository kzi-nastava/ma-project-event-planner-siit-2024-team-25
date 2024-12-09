package com.team25.event.planner.offering.Api;

import com.team25.event.planner.offering.model.OfferingCategory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OfferingCategoryApi {
    @GET("api/offering-categories/")
    Call<List<OfferingCategory>> getOfferingCategories();
}
