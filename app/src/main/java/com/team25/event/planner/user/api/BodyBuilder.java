package com.team25.event.planner.user.api;

import com.team25.event.planner.user.model.EventOrganizerInfo;
import com.team25.event.planner.user.model.OwnerInfo;
import com.team25.event.planner.user.model.RegisterRequest;
import com.team25.event.planner.user.model.UserRequest;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class BodyBuilder {
    public static RequestBody getRegisterFormData(RegisterRequest request) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", request.getEmail())
                .addFormDataPart("password", request.getPassword())
                .addFormDataPart("firstName", request.getFirstName())
                .addFormDataPart("lastName", request.getLastName())
                .addFormDataPart("userRole", request.getUserRole().toString());

        File profilePic = request.getProfilePicture();
        if (profilePic != null && profilePic.exists()) {
            RequestBody imageRequestBody = RequestBody.create(
                    MediaType.parse("image/*"),
                    profilePic
            );
            builder.addFormDataPart("profilePicture", profilePic.getName(), imageRequestBody);
        }

        EventOrganizerInfo eventOrganizerFields = request.getEventOrganizerFields();
        if (eventOrganizerFields != null) {
            if (eventOrganizerFields.getLivingAddress() != null) {
                builder.addFormDataPart("eventOrganizerFields.livingAddress.country", eventOrganizerFields.getLivingAddress().getCountry())
                        .addFormDataPart("eventOrganizerFields.livingAddress.city", eventOrganizerFields.getLivingAddress().getCity())
                        .addFormDataPart("eventOrganizerFields.livingAddress.address", eventOrganizerFields.getLivingAddress().getAddress());
            }
            builder.addFormDataPart("eventOrganizerFields.phoneNumber", eventOrganizerFields.getPhoneNumber());
        }

        OwnerInfo ownerFields = request.getOwnerFields();
        if (ownerFields != null) {
            builder.addFormDataPart("ownerFields.companyName", ownerFields.getCompanyName())
                    .addFormDataPart("ownerFields.contactPhone", ownerFields.getContactPhone())
                    .addFormDataPart("ownerFields.description", ownerFields.getDescription());

            if (ownerFields.getCompanyAddress() != null) {
                builder.addFormDataPart("ownerFields.companyAddress.country", ownerFields.getCompanyAddress().getCountry())
                        .addFormDataPart("ownerFields.companyAddress.city", ownerFields.getCompanyAddress().getCity())
                        .addFormDataPart("ownerFields.companyAddress.address", ownerFields.getCompanyAddress().getAddress());
            }
        }

        return builder.build();
    }

    public static RequestBody getUserFormData(UserRequest request) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("firstName", request.getFirstName())
                .addFormDataPart("lastName", request.getLastName())
                .addFormDataPart("userRole", request.getUserRole().toString());

        File profilePic = request.getProfilePicture();
        if (profilePic != null && profilePic.exists()) {
            RequestBody imageRequestBody = RequestBody.create(
                    MediaType.parse("image/*"),
                    profilePic
            );
            builder.addFormDataPart("profilePicture", profilePic.getName(), imageRequestBody);
        }

        EventOrganizerInfo eventOrganizerFields = request.getEventOrganizerFields();
        if (eventOrganizerFields != null) {
            if (eventOrganizerFields.getLivingAddress() != null) {
                builder.addFormDataPart("eventOrganizerFields.livingAddress.country", eventOrganizerFields.getLivingAddress().getCountry())
                        .addFormDataPart("eventOrganizerFields.livingAddress.city", eventOrganizerFields.getLivingAddress().getCity())
                        .addFormDataPart("eventOrganizerFields.livingAddress.address", eventOrganizerFields.getLivingAddress().getAddress());
            }
            builder.addFormDataPart("eventOrganizerFields.phoneNumber", eventOrganizerFields.getPhoneNumber());
        }

        OwnerInfo ownerFields = request.getOwnerFields();
        if (ownerFields != null) {
            builder.addFormDataPart("ownerFields.companyName", ownerFields.getCompanyName())
                    .addFormDataPart("ownerFields.contactPhone", ownerFields.getContactPhone())
                    .addFormDataPart("ownerFields.description", ownerFields.getDescription());

            if (ownerFields.getCompanyAddress() != null) {
                builder.addFormDataPart("ownerFields.companyAddress.country", ownerFields.getCompanyAddress().getCountry())
                        .addFormDataPart("ownerFields.companyAddress.city", ownerFields.getCompanyAddress().getCity())
                        .addFormDataPart("ownerFields.companyAddress.address", ownerFields.getCompanyAddress().getAddress());
            }
        }

        return builder.build();
    }
}
