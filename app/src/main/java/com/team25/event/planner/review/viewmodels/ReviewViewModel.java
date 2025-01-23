package com.team25.event.planner.review.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.ErrorParse;
import com.team25.event.planner.core.Page;
import com.team25.event.planner.review.api.ReviewApi;
import com.team25.event.planner.review.model.ReviewCard;
import com.team25.event.planner.review.model.ReviewRequestDTO;
import com.team25.event.planner.review.model.ReviewResponseDTO;

import java.lang.invoke.MutableCallSite;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewViewModel extends ViewModel {
    private ReviewApi reviewApi = ConnectionParams.reviewApi;
    private MutableLiveData<List<ReviewResponseDTO>> _reviews = new MutableLiveData<>();
    public LiveData<List<ReviewResponseDTO>> reviews = _reviews;
    private MutableLiveData<Boolean> _eventReview = new MutableLiveData<>();
    public LiveData<Boolean> eventReview = _eventReview;
    private MutableLiveData<Long> _eventId = new MutableLiveData<>();;
    public LiveData<Long> eventId = _eventId;
    private MutableLiveData<Long> _offeringId = new MutableLiveData<>();;
    public LiveData<Long> offeringId = _offeringId;
    private int currentPage;
    private int totalPages;
    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;
    private final MutableLiveData<Boolean> _created = new MutableLiveData<>();
    public final LiveData<Boolean> created = _created;

    public void postReview(ReviewRequestDTO requestDTO){
        reviewApi.createReview(requestDTO).enqueue(new Callback<ReviewCard>() {
            @Override
            public void onResponse(Call<ReviewCard> call, Response<ReviewCard> response) {
                if(response.isSuccessful() && response.body()!= null){
                    _created.postValue(true);
                }else{
                    ErrorParse.catchError(response);
                    _created.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<ReviewCard> call, Throwable t) {
                _created.postValue(false);
            }
        });
    }

    public void getReviewsByEvent(Long eventId){
        reviewApi.getReviewsByEvent(eventId,currentPage).enqueue(new Callback<Page<ReviewResponseDTO>>() {
            @Override
            public void onResponse(Call<Page<ReviewResponseDTO>> call, Response<Page<ReviewResponseDTO>> response) {
                if(response.isSuccessful() && response.body()!= null){
                    _reviews.postValue(response.body().getContent());
                    totalPages = response.body().getTotalPages();
                }else{
                    ErrorParse.catchError(response);
                }
            }

            @Override
            public void onFailure(Call<Page<ReviewResponseDTO>> call, Throwable t) {
                _serverError.postValue("network problem");
            }
        });
    }
    public void getReviewByOffering(Long offeringId){
        reviewApi.getReviewsByOffering(offeringId,currentPage).enqueue(new Callback<Page<ReviewResponseDTO>>() {
            @Override
            public void onResponse(Call<Page<ReviewResponseDTO>> call, Response<Page<ReviewResponseDTO>> response) {
                if(response.isSuccessful() && response.body()!= null){
                    _reviews.postValue(response.body().getContent());
                    totalPages = response.body().getTotalPages();
                }else{
                    ErrorParse.catchError(response);
                }
            }

            @Override
            public void onFailure(Call<Page<ReviewResponseDTO>> call, Throwable t) {
                _serverError.postValue("network problem");
            }
        });
    }

    public void getReviews(){
        if(Boolean.TRUE.equals(eventReview.getValue())){
            getReviewsByEvent(eventId.getValue());
        }else{
            getReviewByOffering(offeringId.getValue());
        }
    }

    public ReviewViewModel(){
        currentPage = 0;
    }

    public void getNextPage(){
        if(this.currentPage+1 < this.totalPages){
            this.currentPage++;
            getReviews();
        }
    }

    public void getPreviousPage(){
        if(this.currentPage > 0){
            this.currentPage--;
            getReviews();
        }
    }
}
