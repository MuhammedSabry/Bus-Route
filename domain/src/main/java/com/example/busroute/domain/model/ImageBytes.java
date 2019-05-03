package com.example.busroute.domain.model;

import com.google.gson.annotations.SerializedName;

public class ImageBytes {

    @SerializedName("bus_image_bytes")
    private String image;

    public ImageBytes(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
