package com.team25.event.planner.user.model;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@Data
@AllArgsConstructor
public class RegisterQuickRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private File profilePicture;
    private UserRole userRole;
    private String invitationCode;

    public RequestBody buildBody(){
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .addFormDataPart("firstName", firstName)
                .addFormDataPart("lastName", lastName)
                .addFormDataPart("userRole", userRole.toString())
                .addFormDataPart("invitationCode", invitationCode);

        if (profilePicture != null && profilePicture.exists()) {
            RequestBody imageRequestBody = RequestBody.create(
                    MediaType.parse("image/*"),
                    profilePicture
            );
            builder.addFormDataPart("profilePicture", profilePicture.getName(), imageRequestBody);
        }

        return builder.build();
    }
}
