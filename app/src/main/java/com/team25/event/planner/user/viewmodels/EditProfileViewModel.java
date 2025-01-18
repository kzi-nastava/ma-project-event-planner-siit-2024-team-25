package com.team25.event.planner.user.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.api.ResponseCallback;
import com.team25.event.planner.user.api.BodyBuilder;
import com.team25.event.planner.user.api.UserApi;
import com.team25.event.planner.user.model.Administrator;
import com.team25.event.planner.user.model.EventOrganizer;
import com.team25.event.planner.user.model.EventOrganizerInfo;
import com.team25.event.planner.user.model.Location;
import com.team25.event.planner.user.model.Owner;
import com.team25.event.planner.user.model.OwnerInfo;
import com.team25.event.planner.user.model.RegularUser;
import com.team25.event.planner.user.model.UserRequest;
import com.team25.event.planner.user.model.UserRole;

import java.io.File;

import lombok.Builder;
import lombok.Data;

public class EditProfileViewModel extends ViewModel {
    private final UserApi userApi = ConnectionParams.userApi;
    private Long userId;

    private final MutableLiveData<RegularUser> _user = new MutableLiveData<>();
    public final LiveData<RegularUser> user = _user;

    public final MutableLiveData<String> firstName = new MutableLiveData<>();
    public final MutableLiveData<String> lastName = new MutableLiveData<>();
    public final MutableLiveData<File> profilePicture = new MutableLiveData<>();
    public final MutableLiveData<String> addressCountry = new MutableLiveData<>();
    public final MutableLiveData<String> addressCity = new MutableLiveData<>();
    public final MutableLiveData<String> address = new MutableLiveData<>();
    public final MutableLiveData<String> phoneNumber = new MutableLiveData<>();

    private final MutableLiveData<String> _serverError = new MutableLiveData<>();
    public final LiveData<String> serverError = _serverError;

    private final MutableLiveData<Boolean> _navigateBack = new MutableLiveData<>(false);
    public final LiveData<Boolean> navigateBack = _navigateBack;

    public final LiveData<Boolean> isEventOrganizer = Transformations.map(_user, user -> user instanceof EventOrganizer);

    @Data
    @Builder
    public static class ErrorUiState {
        private final String firstName;
        private final String lastName;
        private final String addressCountry;
        private final String addressCity;
        private final String address;
        private final String phoneNumber;
    }

    private final MutableLiveData<ErrorUiState> _errors = new MutableLiveData<>();
    public final LiveData<ErrorUiState> errors = _errors;

    public void setUserId(Long userId) {
        this.userId = userId;
        if (userId != null) {
            userApi.getUser(userId).enqueue(new ResponseCallback<>(_user::postValue,
                    _serverError, "EditProfileViewModel"));
        }
    }

    public void setUserFields(RegularUser user) {
        firstName.setValue(user.getFirstName());
        lastName.setValue(user.getLastName());

        if (user instanceof EventOrganizer) {
            EventOrganizer organizer = (EventOrganizer) user;
            Location location = organizer.getLivingAddress();
            addressCountry.setValue(location.getCountry());
            addressCity.setValue(location.getCity());
            address.setValue(location.getAddress());
            phoneNumber.setValue(organizer.getPhoneNumber());
        }
    }

    private boolean validateForm() {
        ErrorUiState.ErrorUiStateBuilder errorBuilder = ErrorUiState.builder();
        boolean isValid = true;

        if (firstName.getValue() == null || firstName.getValue().isBlank()) {
            errorBuilder.firstName("First name is required");
            isValid = false;
        }

        if (lastName.getValue() == null || lastName.getValue().isBlank()) {
            errorBuilder.lastName("Last name is required");
            isValid = false;
        }

        if (isEventOrganizer.getValue() != null && isEventOrganizer.getValue()) {
            if (addressCountry.getValue() == null || addressCountry.getValue().isBlank()) {
                errorBuilder.addressCountry("Country is required");
                isValid = false;
            }

            if (addressCity.getValue() == null || addressCity.getValue().isBlank()) {
                errorBuilder.addressCity("City is required");
                isValid = false;
            }

            if (address.getValue() == null || address.getValue().isBlank()) {
                errorBuilder.address("Address is required");
                isValid = false;
            }

            if (phoneNumber.getValue() == null || phoneNumber.getValue().isBlank()) {
                errorBuilder.phoneNumber("Phone number is required");
                isValid = false;
            }
        }

        _errors.setValue(errorBuilder.build());
        return isValid;
    }

    public void onSubmit() {
        if (!validateForm()) return;

        Owner owner = null;
        RegularUser regularUser = this.user.getValue();
        if (regularUser == null) return;

        if (regularUser instanceof Owner) {
            regularUser.setUserRole(UserRole.OWNER);
            owner = (Owner) regularUser;
        } else if (regularUser instanceof EventOrganizer) {
            regularUser.setUserRole(UserRole.EVENT_ORGANIZER);
        } else if (regularUser instanceof Administrator) {
            regularUser.setUserRole(UserRole.ADMINISTRATOR);
        } else {
            regularUser.setUserRole(UserRole.REGULAR);
        }

        UserRequest userRequest = new UserRequest(
                firstName.getValue(),
                lastName.getValue(),
                profilePicture.getValue(),
                regularUser.getUserRole(),
                regularUser.getUserRole().equals(UserRole.EVENT_ORGANIZER)
                        ? new EventOrganizerInfo(
                        new Location(
                                addressCountry.getValue(),
                                addressCity.getValue(),
                                address.getValue()
                        ),
                        phoneNumber.getValue()
                )
                        : null,
                owner != null ? new OwnerInfo(
                        owner.getCompanyName(),
                        owner.getCompanyAddress(),
                        owner.getContactPhone(),
                        owner.getDescription()
                )
                        : null
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