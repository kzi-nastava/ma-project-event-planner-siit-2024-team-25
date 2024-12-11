package com.team25.event.planner.core.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.SharedPrefService;
import com.team25.event.planner.user.model.User;

public class AuthViewModel extends ViewModel {
    private static final String JWT_KEY = "__JWT";

    private final MutableLiveData<User> _user = new MutableLiveData<>();
    public final LiveData<User> user = _user;

    private final MutableLiveData<String> _jwt = new MutableLiveData<>();
    public final LiveData<String> jwt = _jwt;

    private SharedPrefService sharedPrefService;

    public void initialize(SharedPrefService sharedPrefService) {
        this.sharedPrefService = sharedPrefService;
        _user.postValue(sharedPrefService.getUser());
        _jwt.postValue(sharedPrefService.getString(JWT_KEY));
    }

    public void setUser(User user) {
        _user.postValue(user);
        sharedPrefService.putUser(user);
    }

    public void clearUser() {
        _user.postValue(null);
        sharedPrefService.clearUser();
    }

    public void setJwt(String jwt) {
        _jwt.postValue(jwt);
        sharedPrefService.putString(JWT_KEY, jwt);
    }

    public void clearJwt() {
        _jwt.postValue(null);
        sharedPrefService.clearKey(JWT_KEY);
    }
}
