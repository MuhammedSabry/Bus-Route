package com.example.busroute.model;

import com.example.busroute.domain.model.Bus;

public interface BusListener extends BaseListener {
    void onResult(Bus result);
}