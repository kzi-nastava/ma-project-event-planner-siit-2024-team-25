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
import com.team25.event.planner.user.api.BodyBuilder;
import com.team25.event.planner.user.api.UserApi;
import com.team25.event.planner.user.model.EventOrganizerInfo;
import com.team25.event.planner.user.model.Location;
import com.team25.event.planner.user.model.OwnerInfo;
import com.team25.event.planner.user.model.RegisterQuickRequest;
import com.team25.event.planner.user.model.RegisterQuickResponse;
import com.team25.event.planner.user.model.RegisterRequest;
import com.team25.event.planner.user.model.UserRole;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterQuickViewModel extends ViewModel {
    private final UserApi userApi;

    public RegisterQuickViewModel(){
        this.userApi = ConnectionParams.userApi;
    }
    private final MutableLiveData<UserRole> _userRole = new MutableLiveData<>();
    public final LiveData<UserRole> userRole = _userRole;

    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    public final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    public final MutableLiveData<String> firstName = new MutableLiveData<>();
    public final MutableLiveData<String> lastName = new MutableLiveData<>();
    public final MutableLiveData<File> profilePicture = new MutableLiveData<>();

    private final MutableLiveData<RegisterViewModel.GeneralInfoErrorUiState> _generalInfoErrors = new MutableLiveData<>();
    public final LiveData<RegisterViewModel.GeneralInfoErrorUiState> generalInfoErrors = _generalInfoErrors;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private final MutableLiveData<Long> _eventId = new MutableLiveData<>();
    public final LiveData<Long> eventId = _eventId;

    public final MutableLiveData<String> invitationCode = new MutableLiveData<>();
    private boolean validateGeneralInfoForm() {
        String email = this.email.getValue();
        String password = this.password.getValue();
        String confirmPassword = this.confirmPassword.getValue();
        String firstName = this.firstName.getValue();
        String lastName = this.lastName.getValue();

        RegisterViewModel.GeneralInfoErrorUiState.GeneralInfoErrorUiStateBuilder errorUiStateBuilder = RegisterViewModel.GeneralInfoErrorUiState.builder();
        boolean isValid = true;

        if (email == null || email.isBlank()) {
            errorUiStateBuilder.email("Email is required.");
            isValid = false;
        } else if (!Validation.isValidEmail(email)) {
            errorUiStateBuilder.email("Please enter a valid email.");
            isValid = false;
        }

        if (password == null || password.isBlank()) {
            errorUiStateBuilder.password("Password is required.");
            isValid = false;
        } else if (!Validation.isValidPassword(password)) {
            errorUiStateBuilder.password("Password must contain at least 8 characters, at least on letter and one digit.");
            isValid = false;
        }

        if (confirmPassword == null || confirmPassword.isBlank()) {
            errorUiStateBuilder.confirmPassword("Please confirm your password.");
            isValid = false;
        } else if (!confirmPassword.equals(password)) {
            errorUiStateBuilder.confirmPassword("Passwords do not match.");
            isValid = false;
        }

        if (firstName == null || firstName.isBlank()) {
            errorUiStateBuilder.firstName("First name is required.");
            isValid = false;
        }

        if (lastName == null || lastName.isBlank()) {
            errorUiStateBuilder.lastName("Last name is required.");
            isValid = false;
        }

        _generalInfoErrors.setValue(errorUiStateBuilder.build());

        return isValid;
    }

    public void registerQuick() {
        if(!this.validateGeneralInfoForm()){
            return;
        }
        RegisterQuickRequest registerRequest = new RegisterQuickRequest(
                email.getValue(),
                password.getValue(),
                firstName.getValue(),
                lastName.getValue(),
                profilePicture.getValue(),
                UserRole.REGULAR,
                invitationCode.getValue()
        );

        userApi.registerQuick(registerRequest.buildBody()).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<RegisterQuickResponse> call,
                    @NonNull Response<RegisterQuickResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    _eventId.setValue(response.body().getEventId());
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        if (errorBody != null) {
                            Gson gson = new Gson();
                            ApiError apiError = gson.fromJson(errorBody.charStream(), ApiError.class);
                            _serverError.postValue(apiError.getMessage());
                            Log.e("RegisterViewModel", "Error: " + apiError.getMessage());
                        } else {
                            _serverError.postValue("Unknown error occurred");
                        }
                    } catch (Exception e) {
                        Log.e("RegisterViewModel", "Error parsing response: " + e.getMessage());
                        _serverError.postValue("Error parsing server response");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterQuickResponse> call, @NonNull Throwable t) {
                Log.e("RegisterViewModel", "Network error on signup: " + t.getMessage());
                _serverError.postValue("Network error");
            }
        });
    }
}
