package com.team25.event.planner.user.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.Validation;
import com.team25.event.planner.core.model.ApiError;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.user.api.LoginApi;
import com.team25.event.planner.user.model.LoginRequest;
import com.team25.event.planner.user.model.LoginResponse;
import com.team25.event.planner.user.model.User;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    private final LoginApi loginApi = ConnectionParams.loginApi;

    @Setter
    private AuthViewModel authViewModel;

    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();

    @Data
    @Builder(toBuilder = true)
    public static class ErrorUiState {
        private final String email;
        private final String password;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private final MutableLiveData<Boolean> _loggedIn = new MutableLiveData<>(false);
    public final LiveData<Boolean> loggedIn = _loggedIn;

    public void onLoginSubmit() {
        if (validateForm()) {
            login();
        }
    }

    private boolean validateForm() {
        boolean isValid = true;
        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();

        if (!Validation.isValidEmail(email.getValue())) {
            errorUiStateBuilder.email("Please enter a valid email.");
            isValid = false;
        }

        if (!Validation.isValidPassword(password.getValue())) {
            errorUiStateBuilder.password("Password must contain at least 8 characters, at least on letter and one digit.");
            isValid = false;
        }

        _errors.setValue(errorUiStateBuilder.build());
        _serverError.setValue(null);

        return isValid;
    }

    private void login() {
        String email = this.email.getValue();
        String password = this.password.getValue();

        loginApi.login(new LoginRequest(email, password)).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<LoginResponse> call,
                    @NonNull Response<LoginResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    authViewModel.setUser(new User(
                            loginResponse.getUserId(),
                            loginResponse.getEmail(),
                            loginResponse.getFullName(),
                            loginResponse.getRole()
                    ));
                    // TODO: set jwt
                    clearFields();
                    _loggedIn.postValue(true);
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            Gson gson = new Gson();
                            ApiError apiError = gson.fromJson(errorBody.charStream(), ApiError.class);
                            _serverError.postValue(apiError.getMessage());
                            Log.e("LoginViewModel", "Error: " + apiError.getMessage());
                        } else {
                            _serverError.postValue("Unknown error occurred");
                        }
                    } catch (Exception e) {
                        Log.e("LoginViewModel", "Error parsing response: " + e.getMessage());
                        _serverError.postValue("Error parsing server response");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Log.e("LoginViewModel", "Network error on login: " + t.getMessage());
                _serverError.postValue("Network error");
            }
        });
    }

    private void clearFields() {
        email.setValue(null);
        password.setValue(null);
    }
}
