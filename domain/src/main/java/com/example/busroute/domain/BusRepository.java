package com.example.busroute.domain;

import com.example.busroute.domain.model.Bus;
import com.example.busroute.domain.model.ImageBytes;

import io.reactivex.Observable;

public interface BusRepository {
    Observable<Bus> getBusByImage(ImageBytes imageBytes);

    Observable<Bus> getBusByBusNumber(int busNumber);
}
