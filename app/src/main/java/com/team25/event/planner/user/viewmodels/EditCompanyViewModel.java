package com.team25.event.planner.user.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.Validation;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.user.api.BodyBuilder;
import com.team25.event.planner.user.api.UserApi;
import com.team25.event.planner.user.model.Location;
import com.team25.event.planner.user.model.Owner;
import com.team25.event.planner.user.model.OwnerInfo;
import com.team25.event.planner.user.model.UserRequest;
import com.team25.event.planner.user.model.UserRole;

import lombok.Builder;
import lombok.Data;

public class EditCompanyViewModel extends ViewModel {
    private final UserApi userApi = ConnectionParams.userApi;
    private Long userId;

    private final MutableLiveData<Owner> _owner = new MutableLiveData<>();
    public final LiveData<Owner> owner = _owner;

    public final MutableLiveData<String> companyName = new MutableLiveData<>();
    public final MutableLiveData<String> addressCountry = new MutableLiveData<>();
    public final MutableLiveData<String> addressCity = new MutableLiveData<>();
    public final MutableLiveData<String> address = new MutableLiveData<>();
    public final MutableLiveData<String> phoneNumber = new MutableLiveData<>("");
    public final MutableLiveData<String> description = new MutableLiveData<>();

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private final MutableLiveData<Boolean> _navigateBack = new MutableLiveData<>(false);
    public final LiveData<Boolean> navigateBack = _navigateBack;

    @Data
    @Builder
    public static class ErrorUiState {
        private final String companyName;
        private final String addressCountry;
        private final String addressCity;
        private final String address;
        private final String phoneNumber;
        private final String description;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;

    public void setUserId(Long userId) {
        this.userId = userId;
        if (userId != null) {
            userApi.getUser(userId).enqueue(new ResponseCallback<>(
                    user -> {
                        _owner.postValue((Owner) user);
                        populateFields((Owner) user);
                    },
                    _serverError, "EditProfileViewModel"
            ));
        }
    }

    public void populateFields(Owner owner) {
        companyName.setValue(owner.getCompanyName());
        addressCountry.setValue(owner.getCompanyAddress().getCountry());
        addressCity.setValue(owner.getCompanyAddress().getCity());
        address.setValue(owner.getCompanyAddress().getAddress());
        phoneNumber.setValue(owner.getContactPhone());
        description.setValue(owner.getDescription());
    }

    private boolean validateForm() {
        String companyName = this.companyName.getValue();
        String addressCountry = this.addressCountry.getValue();
        String addressCity = this.addressCity.getValue();
        String address = this.address.getValue();
        String phoneNumber = this.phoneNumber.getValue();
        String description = this.description.getValue();

        ErrorUiState.ErrorUiStateBuilder errorUiStateBuilder = ErrorUiState.builder();
        boolean isValid = true;

        if (companyName == null || companyName.isEmpty()) {
            errorUiStateBuilder.companyName("Please enter company name");
            isValid = false;
        }

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

        _errors.postValue(errorUiStateBuilder.build());

        return isValid;
    }

    public void onSubmit() {
        if (!validateForm()) return;

        Owner owner = this.owner.getValue();
        if (owner == null) return;

        UserRequest userRequest = new UserRequest(
                owner.getFirstName(),
                owner.getLastName(),
                null,
                UserRole.OWNER,
                null,
                new OwnerInfo(
                        companyName.getValue(),
                        new Location(
                                addressCountry.getValue(),
                                addressCity.getValue(),
                                address.getValue()
                        ),
                        phoneNumber.getValue(),
                        description.getValue()
                )
        );

        userApi.updateUser(userId, BodyBuilder.getUserFormData(userRequest))
                .enqueue(new ResponseCallback<>(
                        user -> _navigateBack.postValue(true),
                        _serverError,
                        "EditProfileViewModel"
                ));
    }

    public void onNavigateBackComplete() {
        _navigateBack.setValue(false);
    }
}