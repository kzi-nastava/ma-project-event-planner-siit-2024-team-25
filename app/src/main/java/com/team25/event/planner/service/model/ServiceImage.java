package com.team25.event.planner.service.model;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceImage {
    private final File newImage;
    private final String existingImageUrl;
    private final boolean isExisting;

    public ServiceImage(File newImage, String existingImageUrl, boolean isExisting) {
        this.newImage = newImage;
        this.existingImageUrl = existingImageUrl;
        this.isExisting = isExisting;
    }

    public static ServiceImage fromFile(File file) {
        return new ServiceImage(file, null, false);}

    public static  ServiceImage fromUrl(String url){
        return new ServiceImage(null, url, true);
    }
}
