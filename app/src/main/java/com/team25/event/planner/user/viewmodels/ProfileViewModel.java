package com.team25.event.planner.user.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.user.api.UserApi;
import com.team25.event.planner.user.model.EventOrganizer;
import com.team25.event.planner.user.model.Owner;
import com.team25.event.planner.user.model.RegularUser;

public class ProfileViewModel extends ViewModel {
    private final UserApi userApi = ConnectionParams.userApi;

    private final MutableLiveData<RegularUser> _user = new MutableLiveData<>();
    public final LiveData<RegularUser> user = _user;

    private final MutableLiveData<String> _email = new MutableLiveData<>();
    public final LiveData<String> email = _email;

    public LiveData<Boolean> isOrganizer = Transformations.map(user, user -> user instanceof EventOrganizer);
    public LiveData<Boolean> isOwner = Transformations.map(user, user -> user instanceof Owner);

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    public void setUserId(Long userId) {
        if (userId == null) {
            _user.postValue(null);
        } else {
            userApi.getUser(userId).enqueue(new ResponseCallback<>(
                    _user::postValue,
                    _serverError, "ProfileViewModel"
            ));
        }
    }

    public void setEmail(String email) {
        _email.postValue(email);
    }
}
