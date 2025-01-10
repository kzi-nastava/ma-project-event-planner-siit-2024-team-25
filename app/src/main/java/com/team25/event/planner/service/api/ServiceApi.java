package com.team25.event.planner.service.api;

import com.team25.event.planner.core.Page;
<<<<<<< HEAD:app/src/main/java/com/team25/event/planner/product_service/api/ServiceApi.java
import com.team25.event.planner.product_service.dto.ServiceCreateRequestDTO;
import com.team25.event.planner.product_service.dto.ServiceCreateResponseDTO;
import com.team25.event.planner.product_service.model.Service;
import com.team25.event.planner.product_service.model.ServiceCard;
=======
import com.team25.event.planner.service.dto.ServiceCreateRequestDTO;
import com.team25.event.planner.service.dto.ServiceCreateResponseDTO;
import com.team25.event.planner.service.model.ServiceCard;
>>>>>>> develop:app/src/main/java/com/team25/event/planner/service/api/ServiceApi.java

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ServiceApi {
    @GET("/api/services")
    Call<Page<ServiceCard>> getServices(@QueryMap Map<String, String> filters
    );
    @GET("/api/services/{id}")
    Call<ServiceCreateResponseDTO> getServiceResponse(@Path("id")Long id);
    @GET("api/services/{id}")
    Call<Service> getService(@Path("id")Long id);
    @POST("/api/services")
    Call<ResponseBody> createService(@Body ServiceCreateRequestDTO requestDTO);
    @PUT("api/services/{id}")
    Call<ResponseBody> updateService(@Path("id")Long id,
                                     @Body ServiceCreateRequestDTO requestDTO);

    @DELETE("api/services/{id}")
    Call<ResponseBody> deleteService(@Path("id") Long id);

}
