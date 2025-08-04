package com.team25.event.planner.core.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.SharedPrefService;
import com.team25.event.planner.user.model.User;

public class AuthViewModel extends ViewModel {
    private static final String JWT_KEY = "__JWT";
    private static final String NOTIFICATION_KEY = "__NOTIFICATIONS";

    private final MutableLiveData<User> _user = new MutableLiveData<>();
    public final LiveData<User> user = _user;

    private final MutableLiveData<String> _jwt = new MutableLiveData<>();
    public final LiveData<String> jwt = _jwt;

    private final MutableLiveData<Boolean> _notification = new MutableLiveData<>();
    public final LiveData<Boolean> notification = _notification;

    private final MutableLiveData<Boolean> _interceptorAdded = new MutableLiveData<>(false);
    public final LiveData<Boolean> interceptorAdded = _interceptorAdded;

    private SharedPrefService sharedPrefService;

    public void initialize(SharedPrefService sharedPrefService) {
        this.sharedPrefService = sharedPrefService;
        _user.postValue(sharedPrefService.getUser());
        _jwt.postValue(sharedPrefService.getString(JWT_KEY));
        _notification.postValue(sharedPrefService.getBoolean(NOTIFICATION_KEY, true));
    }

    public Long getUserId() {
        if (user.getValue() == null) {
            return null;
        }
        return user.getValue().getId();
    }

    public void setUser(User user) {
        _user.postValue(user);
        sharedPrefService.putUser(user);
    }

    public void clearUser() {
        _user.postValue(null);
        this.notificationOn();
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

    public void setInterceptorAdded() {
        if (interceptorAdded.getValue() == null || !interceptorAdded.getValue()) {
            _interceptorAdded.postValue(true);
        }
    }

    public void notificationOn(){
        Boolean on = true;
        _notification.postValue(on);
        sharedPrefService.putBoolean(NOTIFICATION_KEY,on);
    }

    public void notificationOff(){
        Boolean off = false;
        _notification.postValue(off);
        sharedPrefService.putBoolean(NOTIFICATION_KEY,off);
    }
}
