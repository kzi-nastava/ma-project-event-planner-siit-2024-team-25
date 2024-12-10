package com.team25.event.planner.core.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.SharedPrefService;
import com.team25.event.planner.user.model.User;

public class AuthViewModel extends ViewModel {
    private final MutableLiveData<User> _user = new MutableLiveData<>();
    public final LiveData<User> user = _user;

    private SharedPrefService sharedPrefService;

    public void initialize(SharedPrefService sharedPrefService) {
        this.sharedPrefService = sharedPrefService;
        _user.setValue(sharedPrefService.getUser());
    }

    public void setUser(User user) {
        _user.setValue(user);
        sharedPrefService.putUser(user);
    }

    public void clearUser() {
        _user.setValue(null);
        sharedPrefService.clearUser();
    }
}
