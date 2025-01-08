package com.team25.event.planner.offering.Api;

import com.team25.event.planner.core.Page;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.model.ProductCard;
import com.team25.event.planner.offering.model.SubmittedOfferingCategory;
import java.util.Map;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface OfferingApi {
    @GET("/api/offerings/top")
    Call<Page<OfferingCard>> getTopOfferings();

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

    @GET("/api/products/all")
    Call<Page<ProductCard>> getProductsPurchase(
            @Query("page") int page,
            @QueryMap Map<String, Object> filter
    );
    @GET("/api/services/all")
    Call<Page<OfferingCard>> getAllServices(
            @Query("page") int page,
            @QueryMap Map<String, Object> filter
    );

    @GET("api/offerings/submitted")
    Call<List<SubmittedOfferingCategory>> getSubmittedCategories();
    @PUT("api/offerings/{id}/updateCategory")
    Call<ResponseBody> updateOfferingsCategory(@Path("id") Long id,
                                               @Query("categoryId") Long categoryId,
                                               @Query("updateCategoryId") Long updateCategoryId);
}
