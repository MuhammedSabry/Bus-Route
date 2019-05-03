package com.example.busroute.dagger;


import com.example.busroute.BusRouteApplication;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ViewModelModule.class})
public interface ApplicationComponents {

    void inject(BusRouteApplication application);
}
