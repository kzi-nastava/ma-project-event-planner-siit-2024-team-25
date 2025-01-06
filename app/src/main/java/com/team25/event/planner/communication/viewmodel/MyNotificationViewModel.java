package com.team25.event.planner.communication.viewmodel;

import static androidx.test.InstrumentationRegistry.getContext;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.communication.api.NotificationApi;
import com.team25.event.planner.communication.model.Notification;
import com.team25.event.planner.communication.model.NotificationRequestDTO;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.core.api.SideEffectResponseCallback;
import com.team25.event.planner.core.viewmodel.AuthViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyNotificationViewModel extends ViewModel {
    private NotificationApi _notificationApi = ConnectionParams.notificationApi;
    private int _currentPage = 0;
    private boolean _isEndReached = false;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public final LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private final MutableLiveData<Notification> _notification = new MutableLiveData<>();
    public LiveData<Notification> notification = _notification;
    private final MutableLiveData<List<Notification>> _notifications = new MutableLiveData<>();
    public LiveData<List<Notification>> notifications = _notifications;

    public void loadNextPage() {
        if (isLoading()) return;
        _isLoading.setValue(true);

        if (_isEndReached) return;

        _notificationApi.getMyNotifications(_currentPage).enqueue(new SideEffectResponseCallback<>(
                page -> {
                    _currentPage++;
                    if (page.isLast()) {
                        _isEndReached = true;
                    }
                    _notifications.postValue(page.getContent());
                },
                () -> _isLoading.postValue(false),
                _serverError, "NotificationViewModel")
        );
    }

    public boolean isEndReached() {
        return _isEndReached;
    }

    public boolean isLoading() {
        return isLoading.getValue() == null || isLoading.getValue();
    }

    public void updateNotification(Notification notification) {
        NotificationRequestDTO notificationRequestDTO = NotificationRequestDTO.builder()
                .id(notification.getId())
                .isViewed(notification.getIsViewed())
                .build();
        _notificationApi.toggleViewed(notificationRequestDTO).enqueue(new ResponseCallback<>(
                _notification::postValue,
                _serverError,
                "NotificationViewModel"
        ));
    }
}
