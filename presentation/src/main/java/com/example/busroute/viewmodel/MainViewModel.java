package com.example.busroute.viewmodel;

import android.graphics.Bitmap;
import android.util.Base64;

import com.example.busroute.domain.interactor.GetBusByBusNumberInteractor;
import com.example.busroute.domain.interactor.GetBusByImageInteractor;
import com.example.busroute.domain.model.Bus;
import com.example.busroute.domain.model.ImageBytes;
import com.example.busroute.model.BusListener;
import com.example.busroute.util.TextUtil;
import com.example.busroute.util.ValidationUtil;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends BaseViewModel {

    private final GetBusByImageInteractor getBusByImageInteractor;
    private final GetBusByBusNumberInteractor getBusByBusNumberInteractor;

    private Bus loadedBus;

    @Inject
    MainViewModel(GetBusByImageInteractor getBusByImageInteractor,
                  GetBusByBusNumberInteractor getBusByBusNumberInteractor) {
        super();
        this.getBusByImageInteractor = getBusByImageInteractor;
        this.getBusByBusNumberInteractor = getBusByBusNumberInteractor;
    }

    public void getBus(String busNumber, BusListener listener) {
        if (!ValidationUtil.isValidInt(busNumber, 0, 2000)) {
            listener.onFail("Invalid bus number");
            return;
        }
        addDisposable(getBusByBusNumberInteractor.execute(TextUtil.integerOf(busNumber))
                .subscribe(bus -> {
                    this.loadedBus = bus;
                    listener.onResult(bus);
                }, error -> listener.onFail(error.getMessage())));
    }

    public void getBus(Bitmap busImage, BusListener listener) {
        if (busImage == null) {
            listener.onFail("Invalid bus image");
            return;
        }

        addDisposable(Single.just(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(integer -> {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    busImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    return Base64.encodeToString(byteArray, Base64.DEFAULT);
                }).flatMap(encodedImage -> getBusByImageInteractor.execute(new ImageBytes(encodedImage)))
                .subscribe(bus -> {
                    this.loadedBus = bus;
                    listener.onResult(bus);
                }, error -> listener.onFail(error.getMessage())));
    }

    public Bus getLoadedBus() {
        return loadedBus;
    }
}
