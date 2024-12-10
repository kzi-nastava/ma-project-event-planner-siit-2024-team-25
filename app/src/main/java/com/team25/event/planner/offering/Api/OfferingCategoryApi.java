package com.team25.event.planner.offering.Api;

import com.team25.event.planner.offering.model.OfferingCategory;

import java.util.List;

import lombok.experimental.Delegate;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OfferingCategoryApi {
    @GET("api/offering-categories/")
    Call<List<OfferingCategory>> getOfferingCategories();
    @POST("api/offering-categories/")
    Call<OfferingCategory> createOfferingCategory(@Body OfferingCategory offeringCategory);
    @PUT("api/offering-categories/{id}")
    Call<OfferingCategory> editOfferingCategory(@Path("id") Long id, @Body OfferingCategory offeringCategory);
    @DELETE("api/offering-categories/{id}")
    Call<ResponseBody> deleteOfferingCategory(@Path("id") Long id);

}
