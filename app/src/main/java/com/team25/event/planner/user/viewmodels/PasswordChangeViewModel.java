package com.team25.event.planner.user.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.Validation;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.user.api.AccountApi;
import com.team25.event.planner.user.model.PasswordChangeRequest;

import lombok.Builder;
import lombok.Data;

public class PasswordChangeViewModel extends ViewModel {
    private final AccountApi accountApi = ConnectionParams.accountApi;

    public final MutableLiveData<String> oldPassword = new MutableLiveData<>();
    public final MutableLiveData<String> newPassword = new MutableLiveData<>();
    public final MutableLiveData<String> confirmNewPassword = new MutableLiveData<>();

    private final MutableLiveData<Boolean> _successSignal = new MutableLiveData<>(false);
    public final LiveData<Boolean> successSignal = _successSignal;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    @Data
    @Builder
    public static class ErrorUiState {
        private final String oldPassword;
        private final String newPassword;
        private final String confirmNewPassword;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;


    private boolean validateForm() {
        String oldPassword = this.oldPassword.getValue();
        String newPassword = this.newPassword.getValue();
        String confirmNewPassword = this.confirmNewPassword.getValue();

        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();
        boolean isValid = true;

        if (oldPassword == null || oldPassword.isEmpty()) {
            errorUiStateBuilder.oldPassword("Please enter the old password");
            isValid = false;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            errorUiStateBuilder.newPassword("Please enter the new password.");
            isValid = false;
        } else if (!Validation.isValidPassword(newPassword)) {
            errorUiStateBuilder.newPassword("Password must contain at least 8 characters, at least on letter and one digit.");
            isValid = false;
        }

        if (confirmNewPassword == null || confirmNewPassword.isEmpty()) {
            errorUiStateBuilder.confirmNewPassword("Please confirm your new password.");
            isValid = false;
        } else if (!confirmNewPassword.equals(newPassword)) {
            errorUiStateBuilder.confirmNewPassword("Passwords do not match.");
            isValid = false;
        }

        _errors.setValue(errorUiStateBuilder.build());

        return isValid;
    }

    public void onSubmit() {
        if (!validateForm()) return;

        final PasswordChangeRequest request = new PasswordChangeRequest(
                oldPassword.getValue(),
                newPassword.getValue()
        );

        accountApi.changePassword(request).enqueue(new ResponseCallback<>(
                (ignored) -> _successSignal.postValue(true),
                _serverError,
                "PasswordChangeViewModel"
        ));
    }

    public void onSuccessHandleComplete() {
        _successSignal.setValue(false);
    }
}