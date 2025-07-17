package com.team25.event.planner.service.api;

import com.team25.event.planner.core.Page;

import com.team25.event.planner.service.dto.ServiceCreateRequestDTO;
import com.team25.event.planner.service.dto.ServiceCreateResponseDTO;
import com.team25.event.planner.service.model.Service;
import com.team25.event.planner.service.model.ServiceCard;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ServiceApi {
    @GET("/api/services")
    Call<Page<Service>> getServices(@QueryMap Map<String, String> filters
    );
    @GET("/api/services/{id}")
    Call<ServiceCreateResponseDTO> getServiceResponse(@Path("id")Long id);
    @GET("api/services/{id}")
    Call<Service> getService(@Path("id")Long id);
    @Multipart
    @POST("api/services")
    Call<ResponseBody> createService(
            @PartMap Map<String, RequestBody> partMap,
            @Part List<MultipartBody.Part> images
    );

    @POST("api/services")
    Call<ResponseBody> createService(@Body ServiceCreateRequestDTO requestDTO);
    @Multipart
    @PUT("api/services/{id}")
    Call<ResponseBody> updateService(@Path("id")Long id,
                                     @PartMap Map<String, RequestBody> requestDTO, @Part List<MultipartBody.Part> images);

    @DELETE("api/services/{id}")
    Call<ResponseBody> deleteService(@Path("id") Long id);

}
