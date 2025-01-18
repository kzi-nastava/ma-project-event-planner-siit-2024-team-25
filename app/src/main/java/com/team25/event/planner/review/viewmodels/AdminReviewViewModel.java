package com.team25.event.planner.review.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.communication.api.NotificationApi;
import com.team25.event.planner.communication.model.Notification;
import com.team25.event.planner.communication.model.NotificationRequestDTO;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.core.api.SideEffectResponseCallback;
import com.team25.event.planner.review.api.ReviewApi;
import com.team25.event.planner.review.model.ReviewCard;
import com.team25.event.planner.review.model.ReviewStatus;

import java.util.List;

public class AdminReviewViewModel extends ViewModel {

    private ReviewApi _reviewApi = ConnectionParams.reviewApi;
    private int _currentPage = 0;
    private boolean _isEndReached = false;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private final MutableLiveData<ReviewCard> _review = new MutableLiveData<>();
    public LiveData<ReviewCard> review = _review;
    private final MutableLiveData<List<ReviewCard>> _reviews = new MutableLiveData<>();
    public LiveData<List<ReviewCard>> reviews = _reviews;



    public void loadNextPage() {
        if (isLoading()) return;
        _isLoading.setValue(true);

        if (_isEndReached) return;

        _reviewApi.getReviews(_currentPage, ReviewStatus.PENDING).enqueue(new SideEffectResponseCallback<>(
                page -> {
                    _currentPage++;
                    if (page.isLast()) {
                        _isEndReached = true;
                    }
                    _reviews.postValue(page.getContent());
                },
                () -> _isLoading.postValue(false),
                _serverError, "AdminReviewViewModel")
        );
    }
    public boolean isEndReached() {
        return _isEndReached;
    }

    public boolean isLoading() {
        return isLoading.getValue() == null || isLoading.getValue();
    }

    public void updateReview(ReviewCard reviewCard) {
    }
}
