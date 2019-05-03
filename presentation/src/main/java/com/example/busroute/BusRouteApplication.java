package com.example.busroute;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

import com.example.busroute.dagger.ApplicationComponents;
import com.example.busroute.dagger.ApplicationModule;
import com.example.busroute.dagger.DaggerApplicationComponents;
import com.example.busroute.viewmodel.ViewModelFactory;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

public class BusRouteApplication extends Application {

    private static ViewModelFactory viewModelFactory;

    public static ViewModelProvider.Factory getViewModelFactory() {
        return viewModelFactory;
    }

    @Inject
    void setViewModelFactory(ViewModelFactory viewModelFactory) {
        BusRouteApplication.viewModelFactory = viewModelFactory;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationComponents applicationComponent = DaggerApplicationComponents.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);

        Toasty.Config.getInstance().allowQueue(false).apply();
    }
}
