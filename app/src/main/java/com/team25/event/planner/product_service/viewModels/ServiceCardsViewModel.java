package com.team25.event.planner.product_service.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectiongParams;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.product_service.api.ServiceApi;
import com.team25.event.planner.product_service.model.ServiceCard;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceCardsViewModel extends ViewModel {
    private final MutableLiveData<List<ServiceCard>> _services = new MutableLiveData<>(new ArrayList<>());
    public final LiveData<List<ServiceCard>> services = _services;

    public ServiceCardsViewModel(){
        List<ServiceCard> serviceCards = new ArrayList<>();
        getServices();

    }

    private void getServices(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConnectiongParams.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceApi serviceApi = retrofit.create(ServiceApi.class);
        Call<Page<ServiceCard>> call = serviceApi.getServices();

        call.enqueue(new Callback<Page<ServiceCard>>() {
            @Override
            public void onResponse(Call<Page<ServiceCard>> call, Response<Page<ServiceCard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _services.setValue(response.body().getContent());
                } else {
                    Log.e("ServiceCardsViewModel", "Error fetching top events: ");
                }
            }

            @Override
            public void onFailure(Call<Page<ServiceCard>> call, Throwable t) {
                Log.e("ServiceCardsViewModel", "Error fetching top events: " + t.getMessage());
            }
        });
    }
}
