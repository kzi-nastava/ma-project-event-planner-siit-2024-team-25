package com.team25.event.planner.product.model;

import java.io.File;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@Data
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private Double discount;
    private List<File> images;
    private Boolean isVisible;
    private Boolean isAvailable;
    private List<Long> eventTypeIds;
    private Long offeringCategoryId;
    private String offeringCategoryName;
    private Long ownerId;
    private List<String> imagesToDelete;

    public RequestBody buildBody() {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", name)
                .addFormDataPart("description", description)
                .addFormDataPart("price", price.toString())
                .addFormDataPart("discount", discount.toString())
                .addFormDataPart("isVisible", isVisible ? "true" : "false")
                .addFormDataPart("isAvailable", isAvailable ? "true" : "false")
                .addFormDataPart("ownerId", ownerId.toString());

        if (offeringCategoryId != null) {
            builder.addFormDataPart("offeringCategoryId", offeringCategoryId.toString());
        } else {
            builder.addFormDataPart("offeringCategoryName", offeringCategoryName);
        }

        if (eventTypeIds != null && !eventTypeIds.isEmpty()) {
            for (Long eventTypeId : eventTypeIds) {
                builder.addFormDataPart("eventTypeIds", eventTypeId.toString());
            }
        }

        if (images != null && !images.isEmpty()) {
            for (File image : images) {
                if (image != null && image.exists()) {
                    RequestBody imageRequestBody = RequestBody.create(
                            MediaType.parse("image/*"),
                            image
                    );
                    builder.addFormDataPart("images", image.getName(), imageRequestBody);
                }
            }
        }

        if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
            for (String imageToDelete : imagesToDelete) {
                builder.addFormDataPart("imagesToDelete", imageToDelete);
            }
        }

        return builder.build();
    }
}
