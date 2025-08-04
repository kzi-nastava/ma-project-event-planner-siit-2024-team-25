package com.team25.event.planner.product.api;

import com.team25.event.planner.core.Page;
import com.team25.event.planner.product.model.MyProductCard;
import com.team25.event.planner.product.model.Product;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ProductApi {
    @GET("api/products/owner/{ownerId}")
    Call<Page<MyProductCard>> getOwnerProducts(
            @Path("ownerId") Long ownerId,
            @Query("page") int page,
            @QueryMap Map<String, Object> filters
    );

    @GET("api/products/{productId}")
    Call<Product> getProduct(@Path("productId") Long productId);

    @POST("api/products")
    Call<Product> createProduct(@Body RequestBody body);

    @PUT("api/products/{productId}")
    Call<Product> updateProject(@Path("productId") Long productId, @Body RequestBody body);

    @DELETE("api/products/{productId}")
    Call<Void> deleteProduct(@Path("productId") Long productId);
}
