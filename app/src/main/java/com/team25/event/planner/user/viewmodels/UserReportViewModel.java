package com.team25.event.planner.user.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.user.api.UserReportApi;
import com.team25.event.planner.user.model.UserReportRequest;

public class UserReportViewModel extends ViewModel {
    private final UserReportApi _userReportApi = ConnectionParams.userReportApi;

    private final Long _reportedUserId;

    public UserReportViewModel(Long reportedUserId) {
        this._reportedUserId = reportedUserId;
        _currentReport.setValue(new UserReportRequest());
    }

    private final MutableLiveData<Boolean> _isReportSend = new MutableLiveData<>(false);
    public LiveData<Boolean> isReportSend = _isReportSend;

    private final MutableLiveData<UserReportRequest> _currentReport = new MutableLiveData<>();
    public LiveData<UserReportRequest> currentReport = _currentReport;
    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public void reportUser() {
        this._currentReport.getValue().setReportedUserId(this._reportedUserId);
        _userReportApi.reportUser(this._currentReport.getValue()).enqueue(new ResponseCallback<>(
                (report) -> {
                    _isReportSend.postValue(true);
                },
                _serverError, "UserReportViewModel"));
    }

    public void onReportDone() {
        _isReportSend.postValue(false);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Long userId;

        public Factory(Long userId) {
            this.userId = userId;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(UserReportViewModel.class)) {
                return (T) new UserReportViewModel(userId);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
