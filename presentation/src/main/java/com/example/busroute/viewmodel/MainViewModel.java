package com.example.busroute.viewmodel;

import android.graphics.Bitmap;
import android.util.Base64;

import androidx.lifecycle.MutableLiveData;

import com.example.busroute.domain.interactor.GetBusByBusNumberInteractor;
import com.example.busroute.domain.interactor.GetBusByImageInteractor;
import com.example.busroute.domain.model.Bus;
import com.example.busroute.domain.model.ImageBytes;
import com.example.busroute.model.BusListener;
import com.example.busroute.util.TextUtil;
import com.example.busroute.util.ValidationUtil;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

public class MainViewModel extends BaseViewModel {

    private final GetBusByImageInteractor getBusByImageInteractor;
    private final GetBusByBusNumberInteractor getBusByBusNumberInteractor;

    private final MutableLiveData<Bus> busLiveData;

    private Bus loadedBus;

    @Inject
    MainViewModel(GetBusByImageInteractor getBusByImageInteractor,
                  GetBusByBusNumberInteractor getBusByBusNumberInteractor) {
        super();
        this.getBusByImageInteractor = getBusByImageInteractor;
        this.getBusByBusNumberInteractor = getBusByBusNumberInteractor;
        busLiveData = new MutableLiveData<>();
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

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        busImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        addDisposable(getBusByImageInteractor.execute(new ImageBytes(encodedImage))
                .subscribe(bus -> {
                    this.loadedBus = bus;
                    listener.onResult(bus);
                }, error -> listener.onFail(error.getMessage())));
    }

    public Bus getLoadedBus() {
        return loadedBus;
    }
}
