package com.team25.event.planner.product.model;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImage {
    private final File newImage;
    private final String existingImageUrl;
    private final boolean isExisting;

    private ProductImage(File newImage, String existingImageUrl, boolean isExisting) {
        this.newImage = newImage;
        this.existingImageUrl = existingImageUrl;
        this.isExisting = isExisting;
    }

    public static ProductImage fromFile(File file) {
        return new ProductImage(file, null, false);
    }

    public static ProductImage fromUrl(String url) {
        return new ProductImage(null, url, true);
    }
}
