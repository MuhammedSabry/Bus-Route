package com.example.busroute.domain.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Zone {
    private int id;
    @SerializedName("zone_text")
    private String name;

    public Zone(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
