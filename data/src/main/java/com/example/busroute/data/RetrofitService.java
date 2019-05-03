package com.example.busroute.data;

import com.example.busroute.domain.model.Bus;
import com.example.busroute.domain.model.ImageBytes;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {

    @POST("bus_service/upload_image")
    Observable<Bus> getBusByImage(@Body ImageBytes imageBytes);

    @POST("bus_service/bus/{busNumber}/zones")
    Observable<Bus> getBusByNumber(@Path("busNumber") int busNumber);

}
