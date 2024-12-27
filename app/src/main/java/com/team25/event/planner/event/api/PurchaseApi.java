package com.team25.event.planner.event.api;

import com.team25.event.planner.offering.model.ProductPurchaseResponseDTO;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PurchaseApi {
    @POST("api/purchase/events/{eventId}/products")
    Call<ProductPurchaseResponseDTO> purchaseProduct(@Path("eventId")Long eventId);
}
