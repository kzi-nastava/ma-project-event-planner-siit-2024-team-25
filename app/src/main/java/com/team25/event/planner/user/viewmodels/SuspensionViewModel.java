package com.team25.event.planner.user.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.core.api.SideEffectResponseCallback;
import com.team25.event.planner.user.api.SuspensionApi;
import com.team25.event.planner.user.model.UserReportResponse;
import com.team25.event.planner.user.model.UserReportUpdateRequest;

import java.util.List;

public class SuspensionViewModel extends ViewModel {

    private SuspensionApi _suspensionApi = ConnectionParams.suspensionApi;

    private boolean _isEndReached = false;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private MutableLiveData<List<UserReportResponse>> _reports = new MutableLiveData<>();
    public LiveData<List<UserReportResponse>> reports = _reports;

    private MutableLiveData<UserReportResponse> _currentReport = new MutableLiveData<>();
    public LiveData<UserReportResponse> currentReport = _currentReport;

    private int _currentPage;

    public SuspensionViewModel(){
        _currentPage = 0;
    }

    public void loadCurrentPage(){
        this._currentPage -= 1;
        this._isEndReached = false;
        loadNextPage();
    }

    public void loadNextPage() {
        if (isLoading()) return;
        _isLoading.setValue(true);

        if (_isEndReached) return;

        _suspensionApi.getAllReports(_currentPage, false).enqueue(new SideEffectResponseCallback<>(
                page -> {
                    _currentPage++;
                    if (page.isLast()) {
                        _isEndReached = true;
                    }
                    _reports.setValue(page.getContent());
                },
                () -> _isLoading.postValue(false),
                _serverError, "SuspensionViewModel")
        );
    }

    public boolean isEndReached() {
        return _isEndReached;
    }

    public boolean isLoading() {
        return isLoading.getValue() == null || isLoading.getValue();
    }


    public void updateReport(UserReportResponse userReportResponse){
        UserReportUpdateRequest request = new UserReportUpdateRequest();
        request.setIsViewed(true);
        request.setReportId(userReportResponse.getId());
        _suspensionApi.updateReport(request).enqueue(new ResponseCallback<>(
                _currentReport::postValue,
                _serverError, "SuspensionViewModel")
        );
    }

    public void suspendUser(){
    }
}
