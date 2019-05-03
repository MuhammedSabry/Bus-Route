package com.example.busroute.model;

public interface BaseListener {

    void onSuccess(String message);

    void onFail(String message);

    void onFallBack();
}
