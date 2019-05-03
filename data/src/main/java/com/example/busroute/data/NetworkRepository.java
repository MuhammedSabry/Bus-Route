package com.example.busroute.data;

import com.example.busroute.domain.BusRepository;
import com.example.busroute.domain.model.Bus;
import com.example.busroute.domain.model.ImageBytes;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkRepository implements BusRepository {

    private static final String BASE_URL = "https://image-processing-bus-services.herokuapp.com/";
    private final RetrofitService serviceApi;

    @Inject
    NetworkRepository() {

        //Configuring the logcat to display request/response parameters
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.networkInterceptors().add(new StethoInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        serviceApi = retrofit.create(RetrofitService.class);
    }

    @Override
    public Observable<Bus> getBusByImage(ImageBytes imageBytes) {
        return serviceApi.getBusByImage(imageBytes);
    }

    @Override
    public Observable<Bus> getBusByBusNumber(int busNumber) {
        return serviceApi.getBusByNumber(busNumber);
    }

    private Completable completableSourceMapper(Response<ResponseBody> response) {
        try {
            responseMapper(response);
            return Completable.complete();
        } catch (Exception e) {
            return Completable.error(e);
        }
    }

    private String stringResponseMapper(Response<ResponseBody> response) throws IOException {

        //If response code is 200 and it contains a body
        if (response.isSuccessful() && response.body() != null)
            return response.body().string();
        else {
            if (response.errorBody() != null)
                throw new RuntimeException(response.errorBody().string());
            else if (response.body() != null)
                throw new RuntimeException(response.body().string());
            else
                throw new HttpException(response);
        }
    }

    private boolean responseMapper(Response<ResponseBody> response) throws IOException {
        if (isSuccessful(response)) {
            return true;
        } else {
            if (response.errorBody() != null)
                throw new RuntimeException(response.errorBody().string());
            else if (response.body() != null)
                throw new RuntimeException(response.body().string());
            else
                throw new HttpException(response);
        }
    }

    private boolean isSuccessful(Response response) {
        return response.isSuccessful();
    }
}
