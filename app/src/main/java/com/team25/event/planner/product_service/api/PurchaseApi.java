package com.team25.event.planner.product_service.api;

import com.team25.event.planner.product_service.dto.ServicePurchaseRequestDTO;
import com.team25.event.planner.product_service.dto.ServicePurchaseResponseDTO;
import com.team25.event.planner.product_service.model.Service;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PurchaseApi {
    @POST("/api/purchase/event/{eventId}/service/{serviceId}")
    Call<ServicePurchaseResponseDTO> bookService(@Path(value = "eventId") Long eventId,
                                                 @Path(value = "serviceId") Long serviceId,
                                                 @Body ServicePurchaseRequestDTO purchase);

    @GET("/api/purchase/service/{serviceId}/available")
    Call<Boolean> isServiceAvailable(@Path(value = "serviceId") Long serviceId,
                                     @QueryMap Map<String, String> purchase);

    @GET("/api/purchase/event/{eventId}/budget")
    Call<Double> getLeftMoneyFromBudgetItem(@Path(value = "eventId") Long eventId,
                                            @Query(value = "serviceId") Long serviceId);
}
