package com.team25.event.planner.product_service.api;

import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.product_service.dto.ServiceCreateRequestDTO;
import com.team25.event.planner.product_service.model.Service;
import com.team25.event.planner.product_service.model.ServiceCard;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceApi {
    @GET("/api/services")
    Call<Page<ServiceCard>> getServices(
    );
    @POST("/api/services")
    Call<ResponseBody> createService(@Body ServiceCreateRequestDTO requestDTO);
}
