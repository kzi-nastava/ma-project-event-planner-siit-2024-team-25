package com.team25.event.planner.user.api;

import android.app.DownloadManager;

import com.team25.event.planner.offering.model.FavoruriteOfferingDTO;
import com.team25.event.planner.offering.model.OfferingCard;
import com.team25.event.planner.offering.model.ProductCard;
import com.team25.event.planner.service.model.Offering;
import com.team25.event.planner.service.model.ServiceCard;
import com.team25.event.planner.user.model.RegisterQuickResponse;
import com.team25.event.planner.user.model.RegisterResponse;
import com.team25.event.planner.user.model.RegularUser;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @POST("/api/auth/register")
    Call<RegisterResponse> register(@Body RequestBody body);

    @POST("/api/auth/register/quick")
    Call<RegisterQuickResponse> registerQuick(@Body RequestBody body);

    @GET("/api/users/{userId}")
    Call<RegularUser> getUser(@Path("userId") Long userId);

    @PUT("/api/users/{userId}")
    Call<RegularUser> updateUser(@Path("userId") Long userId, @Body RequestBody body);
    @POST("/api/auth/upgrade")
    Call<RegisterResponse> upgradeProfile(@Body RequestBody body);

    @GET("api/users/{id}/favourite-services")
    Call<List<OfferingCard>> getFavoriteService(@Path("id") Long id);

    @GET("api/users/{id}/favourite-products")
    Call<List<OfferingCard>> getFavoriteProducts(@Path("id") Long id);
    @POST("api/users/{id}/favourite-products")
    Call<ProductCard> favoriteProduct(@Path("id") Long id, @Body FavoruriteOfferingDTO dto);

    @POST("api/users/{id}/favourite-services")
    Call<ServiceCard> favoriteService(@Path("id") Long id, @Body FavoruriteOfferingDTO dto);
    @DELETE("api/users/{id}/favourite-service/{favId}")
    Call<Void> deleteFavoriteService(@Path("id") Long id, @Path("favId") Long favId);
    @DELETE("api/users/{id}/favourite-product/{favId}")
    Call<Void> deleteFavoriteProduct(@Path("id") Long id, @Path("favId") Long favId);

}
