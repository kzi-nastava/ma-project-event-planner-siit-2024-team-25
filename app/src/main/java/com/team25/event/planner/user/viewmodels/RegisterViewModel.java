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
import com.team25.event.planner.user.model.RegisterRequest;
import com.team25.event.planner.user.model.RegisterResponse;
import com.team25.event.planner.user.model.UserRole;

import java.io.File;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {
    private final UserApi userApi;

    public RegisterViewModel() {
        this.userApi = ConnectionParams.userApi;
    }

    public enum RegisterStep {
        RoleChoice, GeneralInfo, OrganizerSpecific, VendorSpecific, Success
    }

    private final MutableLiveData<RegisterStep> _formStep = new MutableLiveData<>(RegisterStep.RoleChoice);
    public final LiveData<RegisterStep> formStep = _formStep;

    public void clearFormStepNavigation() {
        _formStep.setValue(null);
    }


    private final MutableLiveData<UserRole> _userRole = new MutableLiveData<>();
    public final LiveData<UserRole> userRole = _userRole;

    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    public final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    public final MutableLiveData<String> firstName = new MutableLiveData<>();
    public final MutableLiveData<String> lastName = new MutableLiveData<>();
    public final MutableLiveData<File> profilePicture = new MutableLiveData<>();

    public final MutableLiveData<String> addressCountry = new MutableLiveData<>();
    public final MutableLiveData<String> addressCity = new MutableLiveData<>();
    public final MutableLiveData<String> address = new MutableLiveData<>();
    public final MutableLiveData<String> phoneNumber = new MutableLiveData<>("");
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<List<byte[]>> pictures = new MutableLiveData<>();


    private final MutableLiveData<String> _roleChoiceError = new MutableLiveData<>();
    public final LiveData<String> roleChoiceError = _roleChoiceError;

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    @Data
    @Builder(toBuilder = true)
    public static class GeneralInfoErrorUiState {
        private final String email;
        private final String password;
        private final String confirmPassword;
        private final String firstName;
        private final String lastName;
        private final String profilePicture;
    }

    private final MutableLiveData<GeneralInfoErrorUiState> _generalInfoErrors = new MutableLiveData<>();
    public final LiveData<GeneralInfoErrorUiState> generalInfoErrors = _generalInfoErrors;

    @Data
    @Builder(toBuilder = true)
    public static class OrganizerSpecificErrorUiState {
        private final String addressCountry;
        private final String addressCity;
        private final String address;
        private final String phoneNumber;
    }

    private final MutableLiveData<OrganizerSpecificErrorUiState> _organizerSpecificErrors = new MutableLiveData<>();
    public final LiveData<OrganizerSpecificErrorUiState> organizerSpecificErrors = _organizerSpecificErrors;

    @Data
    @Builder(toBuilder = true)
    public static class VendorSpecificErrorUiState {
        private final String addressCountry;
        private final String addressCity;
        private final String address;
        private final String phoneNumber;
        private final String description;
        private final String pictures;
    }

    private final MutableLiveData<VendorSpecificErrorUiState> _vendorSpecificErrors = new MutableLiveData<>();
    public final LiveData<VendorSpecificErrorUiState> vendorSpecificErrors = _vendorSpecificErrors;

    public void onRoleChoiceNext(UserRole userRole) {
        if (userRole == null) {
            _roleChoiceError.postValue("Please select a user role.");
        } else if (!userRole.equals(UserRole.EVENT_ORGANIZER) && !userRole.equals(UserRole.OWNER)) {
            _roleChoiceError.postValue("Unsupported user type.");
        } else {
            _roleChoiceError.postValue(null);
            _userRole.setValue(userRole);
            _formStep.setValue(RegisterStep.GeneralInfo);
        }
    }

    public void onGeneralInfoBack() {
        _formStep.postValue(RegisterStep.RoleChoice);
    }

    public void onGeneralInfoNext() {
        if (validateGeneralInfoForm()) {
            UserRole userRole = this.userRole.getValue();

            if (userRole != null && userRole.equals(UserRole.EVENT_ORGANIZER)) {
                _formStep.postValue(RegisterStep.OrganizerSpecific);
            } else {
                _formStep.postValue(RegisterStep.VendorSpecific);
            }
        }
    }

    private boolean validateGeneralInfoForm() {
        String email = this.email.getValue();
        String password = this.password.getValue();
        String confirmPassword = this.confirmPassword.getValue();
        String firstName = this.firstName.getValue();
        String lastName = this.lastName.getValue();

        GeneralInfoErrorUiState.GeneralInfoErrorUiStateBuilder errorUiStateBuilder = GeneralInfoErrorUiState.builder();
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

    public void onOrganizerSpecificBack() {
        _formStep.postValue(RegisterStep.GeneralInfo);
    }

    public void onOrganizerSpecificNext() {
        if (validateOrganizerSpecificForm()) {
            register();
        }
    }

    private boolean validateOrganizerSpecificForm() {
        String addressCountry = this.addressCountry.getValue();
        String addressCity = this.addressCity.getValue();
        String address = this.address.getValue();
        String phoneNumber = this.phoneNumber.getValue();

        OrganizerSpecificErrorUiState.OrganizerSpecificErrorUiStateBuilder errorUiStateBuilder = OrganizerSpecificErrorUiState.builder();
        boolean isValid = true;

        if (addressCountry == null || addressCountry.isEmpty()) {
            errorUiStateBuilder.addressCountry("Please enter a state.");
            isValid = false;
        }

        if (addressCity == null || addressCity.isEmpty()) {
            errorUiStateBuilder.addressCity("Please enter a city.");
            isValid = false;
        }

        if (address == null || address.isEmpty()) {
            errorUiStateBuilder.address("Please enter an address.");
            isValid = false;
        }

        if (phoneNumber == null || phoneNumber.isEmpty() || phoneNumber.equals("+")) {
            errorUiStateBuilder.phoneNumber("Please enter a phone number.");
            isValid = false;
        } else if (!Validation.isValidPhoneNumber(phoneNumber)) {
            errorUiStateBuilder.phoneNumber("Invalid phone number format.");
            isValid = false;
        }

        _organizerSpecificErrors.postValue(errorUiStateBuilder.build());

        return isValid;
    }

    public void onVendorSpecificBack() {
        _formStep.postValue(RegisterStep.GeneralInfo);
    }

    public void onVendorSpecificNext() {
        if (validateVendorSpecificForm()) {
            register();
        }
    }

    private boolean validateVendorSpecificForm() {
        String addressCountry = this.addressCountry.getValue();
        String addressCity = this.addressCity.getValue();
        String address = this.address.getValue();
        String phoneNumber = this.phoneNumber.getValue();
        String description = this.description.getValue();
        List<byte[]> pictures = this.pictures.getValue();

        VendorSpecificErrorUiState.VendorSpecificErrorUiStateBuilder errorUiStateBuilder = VendorSpecificErrorUiState.builder();
        boolean isValid = true;

        if (addressCountry == null || addressCountry.isEmpty()) {
            errorUiStateBuilder.addressCountry("Please enter a state.");
            isValid = false;
        }

        if (addressCity == null || addressCity.isEmpty()) {
            errorUiStateBuilder.addressCity("Please enter a city.");
            isValid = false;
        }

        if (address == null || address.isEmpty()) {
            errorUiStateBuilder.address("Please enter an address.");
            isValid = false;
        }

        if (phoneNumber == null || phoneNumber.isEmpty() || phoneNumber.equals("+")) {
            errorUiStateBuilder.phoneNumber("Please enter a phone number.");
            isValid = false;
        } else if (!Validation.isValidPhoneNumber(phoneNumber)) {
            errorUiStateBuilder.phoneNumber("Invalid phone number format.");
            isValid = false;
        }

        if (description == null || description.isEmpty()) {
            errorUiStateBuilder.description("Please enter a description.");
            isValid = false;
        }

        _vendorSpecificErrors.postValue(errorUiStateBuilder.build());

        return isValid;
    }


    public final MutableLiveData<Boolean> goToLogin = new MutableLiveData<>(false);

    public void onGoToLogin() {
        goToLogin.postValue(true);
    }

    public void register() {
        RegisterRequest registerRequest = new RegisterRequest(
                email.getValue(),
                password.getValue(),
                firstName.getValue(),
                lastName.getValue(),
                profilePicture.getValue(),
                userRole.getValue(),
                userRole.getValue() == UserRole.EVENT_ORGANIZER
                        ? new EventOrganizerInfo(
                        new Location(
                                addressCountry.getValue(),
                                addressCity.getValue(),
                                address.getValue()
                        ),
                        phoneNumber.getValue()
                )
                        : null,
                userRole.getValue() == UserRole.OWNER
                        ? new OwnerInfo(
                        null, // company name
                        new Location(
                                addressCountry.getValue(),
                                addressCity.getValue(),
                                address.getValue()
                        ),
                        phoneNumber.getValue(),
                        description.getValue()
                )
                        : null
        );

        userApi.register(BodyBuilder.getRegisterFormData(registerRequest)).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<RegisterResponse> call,
                    @NonNull Response<RegisterResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    _formStep.postValue(RegisterStep.Success);
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
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                Log.e("RegisterViewModel", "Network error on signup: " + t.getMessage());
                _serverError.postValue("Network error");
            }
        });
    }
}
