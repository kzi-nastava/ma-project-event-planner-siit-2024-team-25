package com.team25.event.planner.service.api;

import com.team25.event.planner.event.model.PurchaseResponseDTO;
import com.team25.event.planner.offering.model.ProductPurchaseRequestDTO;
import com.team25.event.planner.offering.model.ProductPurchaseResponseDTO;
import com.team25.event.planner.service.dto.ServicePurchaseRequestDTO;
import com.team25.event.planner.service.dto.ServicePurchaseResponseDTO;
import com.team25.event.planner.user.model.PurchaseServiceCard;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @POST("api/purchase/events/{eventId}/products")
    Call<ProductPurchaseResponseDTO> purchaseProduct(@Path("eventId") Long eventId,
                                                     @Body ProductPurchaseRequestDTO requestDTO);

    @GET("/api/purchase/service/{serviceId}/available")
    Call<Boolean> isServiceAvailable(@Path(value = "serviceId") Long serviceId,
                                     @QueryMap Map<String, String> purchase);

    @GET("/api/purchase/event/{eventId}/budget")
    Call<Double> getLeftMoneyFromBudgetItem(@Path(value = "eventId") Long eventId,
                                            @Query(value = "categoryId") Long categoryId);

    @GET("/api/purchase/")
    Call<List<PurchaseServiceCard>> getOwnerPurchases(
            @Query("ownerId") Long ownerId,
            @Query("startDate") LocalDate startDate,
            @Query("endDate") LocalDate endDate
    );
    @GET("/api/purchase/events/{eventId}")
    Call<List<PurchaseResponseDTO>> getPurchaseByEvent(@Path(value = "eventId")Long eventId);
    @GET("/api/purchase/offerings/{offeringId}")
    Call<List<PurchaseResponseDTO>> getPurchaseByOffering(@Path("offeringId")Long offeringId);
}
