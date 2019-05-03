package com.example.busroute.data;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkRepository {

    private static final String BASE_URL = "https://graduation-server.herokuapp.com/";
    private final RetrofitService serviceApi;

    @Inject
    NetworkRepository() {

        //Configuring the logcat to display request/response parameters
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.networkInterceptors().add(new StethoInterceptor());

        //10 Seconds timeout for reading , writing and connection establishing
        httpClient.connectTimeout(10, TimeUnit.SECONDS);
        httpClient.readTimeout(10, TimeUnit.SECONDS);
        httpClient.writeTimeout(10, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        serviceApi = retrofit.create(RetrofitService.class);
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
