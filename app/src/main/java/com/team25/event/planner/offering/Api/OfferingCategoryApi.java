package com.team25.event.planner.offering.Api;

import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.model.SubmittedOfferingCategory;

import org.junit.runners.Parameterized;

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
import retrofit2.http.Query;

public interface OfferingCategoryApi {
    @GET("api/offering-categories/")
    Call<List<OfferingCategory>> getOfferingCategories();
    @GET("api/offering-categories/{id}")
    Call<OfferingCategory> getOfferingCategory(@Path("id") Long id);
    @POST("api/offering-categories/")
    Call<OfferingCategory> createOfferingCategory(@Body OfferingCategory offeringCategory);
    @PUT("api/offering-categories/{id}")
    Call<OfferingCategory> editOfferingCategory(@Path("id") Long id, @Body OfferingCategory offeringCategory);
    @DELETE("api/offering-categories/{id}")
    Call<ResponseBody> deleteOfferingCategory(@Path("id") Long id);

}
