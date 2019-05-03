package com.example.busroute.domain.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Bus {
    private long id;
    @SerializedName("bus_number")
    private int busNumber;
    private String[] zones;
    private String link;

    public Bus(long id, int busNumber, String[] zones, String link) {
        this.id = id;
        this.busNumber = busNumber;
        this.zones = zones;
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public String[] getZones() {
        return zones;
    }

    public void setZones(String[] zones) {
        this.zones = zones;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @NonNull
    @Override
    public String toString() {
        return "Bus number= " + this.busNumber
                + ", Zones=" + Arrays.toString(zones)
                + ", Link= " + (link == null ? "null" : link);
    }
}
