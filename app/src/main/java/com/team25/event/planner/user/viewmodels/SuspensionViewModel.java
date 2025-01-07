package com.team25.event.planner.user.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.SideEffectResponseCallback;
import com.team25.event.planner.user.api.SuspensionApi;
import com.team25.event.planner.user.model.SuspensionResponse;

import java.util.List;

public class SuspensionViewModel extends ViewModel {

    private SuspensionApi _suspensionApi = ConnectionParams.suspensionApi;

    private boolean _isEndReached = false;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private MutableLiveData<List<SuspensionResponse>> _suspensions = new MutableLiveData<>();
    public LiveData<List<SuspensionResponse>> suspensions = _suspensions;

    private int _currentPage;

    public SuspensionViewModel() {
        _currentPage = 0;
    }

    public void loadCurrentPage() {
        this._currentPage -= 1;
        this._isEndReached = false;
        loadNextPage();
    }

    public void loadNextPage() {
        if (isLoading()) return;
        _isLoading.setValue(true);

        if (_isEndReached) return;

        _suspensionApi.getAllSuspensions(_currentPage).enqueue(new SideEffectResponseCallback<>(
                page -> {
                    _currentPage++;
                    if (page.isLast()) {
                        _isEndReached = true;
                    }
                    _suspensions.setValue(page.getContent());
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

}
