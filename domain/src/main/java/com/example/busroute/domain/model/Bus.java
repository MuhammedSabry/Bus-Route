package com.example.busroute.domain.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Bus {
    private long id;
    @SerializedName("bus_number")
    private int busNumber;
    private Zone[] zones;
    private String link;

    public Bus(long id, int busNumber, Zone[] zones, String link) {
        this.id = id;
        this.busNumber = busNumber;
        this.zones = zones;
        this.link = link;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public Zone[] getZones() {
        return zones;
    }

    public String getLink() {
        return link;
    }

    @NonNull
    @Override
    public String toString() {
        return "Bus number= " + this.busNumber
                + ", Zones=" + Arrays.toString(zones)
                + ", Link= " + (link == null ? "null" : link);
    }
}
