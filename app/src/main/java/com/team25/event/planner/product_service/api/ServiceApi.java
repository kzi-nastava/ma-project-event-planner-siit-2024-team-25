package com.team25.event.planner.product_service.api;

import com.team25.event.planner.core.Page;
import com.team25.event.planner.event.model.EventCard;
import com.team25.event.planner.product_service.dto.ServiceCreateRequestDTO;
import com.team25.event.planner.product_service.dto.ServiceFilterDTO;
import com.team25.event.planner.product_service.model.Service;
import com.team25.event.planner.product_service.model.ServiceCard;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ServiceApi {
    @GET("/api/services")
    Call<Page<ServiceCard>> getServices(@QueryMap Map<String, String> filters
    );
    @POST("/api/services")
    Call<ResponseBody> createService(@Body ServiceCreateRequestDTO requestDTO);

}
