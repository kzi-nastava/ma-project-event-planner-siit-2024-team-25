package com.team25.event.planner.user.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.Validation;
import com.team25.event.planner.user.model.User;

import lombok.Builder;
import lombok.Data;

public class LoginViewModel extends ViewModel {
    public final MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

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

        assert password != null;
        if (password.equals("error123")) {
            _serverError.setValue("Invalid password.");
        } else {
            User user = new User("John", "Smith", email);
            Log.d("DEBUG", user.getEmail());
            _loggedIn.setValue(true);
            clearFields();
        }
    }

    private void clearFields() {
        email.setValue(null);
        password.setValue(null);
    }
}
