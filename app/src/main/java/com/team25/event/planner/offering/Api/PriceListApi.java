package com.team25.event.planner.offering.Api;

import com.team25.event.planner.offering.model.OfferingCategory;
import com.team25.event.planner.offering.model.PriceListItemRequestDTO;
import com.team25.event.planner.offering.model.PriceListItemResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PriceListApi {
    @GET("api/price-list/{ownerId}/products")
    Call<List<PriceListItemResponseDTO>> getProducts(@Path("ownerId") Long ownerId);

    @GET("api/price-list/{ownerId}/services")
    Call<List<PriceListItemResponseDTO>> getServices(@Path("ownerId") Long ownerId);
    @PUT("api/price-list/{offeringId}")
    Call<PriceListItemResponseDTO> editOfferingCategory(@Path("offeringId") Long offeringId, @Body PriceListItemRequestDTO requestDTO);

    @GET("api/price-list/{offeringId}")
    Call<PriceListItemResponseDTO> getPriceListItem(@Path("offeringId") Long offeringId);
}
