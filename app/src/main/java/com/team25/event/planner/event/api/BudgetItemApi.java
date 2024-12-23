package com.team25.event.planner.event.api;

import com.team25.event.planner.event.model.BudgetItemRequestDTO;
import com.team25.event.planner.event.model.BudgetItemResponseDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BudgetItemApi {
    @GET("api/budget-items/")
    Call<List<BudgetItemResponseDTO>> getBudgetItems(@Query("eventId") Long eventId);

    @GET("api/budget-items/{id}")
    Call<BudgetItemResponseDTO> getBudgetItem(@Path("id")Long id,
                                              @Query("eventId") Long eventId);

    @POST("api/budget-items/")
    Call<BudgetItemResponseDTO> createBudgetItem(@Body BudgetItemRequestDTO requestDTO);

    @PUT("api/budget-items/{id}")
    Call<BudgetItemResponseDTO> updateBudgetItem(@Path("id")Long id,
                                                 @Body BudgetItemRequestDTO requestDTO);

    @DELETE("api/budget-items/{id}")
    Call<ResponseBody> deleteBudgetItem(@Path("id")Long id);
}
