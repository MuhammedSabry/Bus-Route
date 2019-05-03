package com.example.busroute.dagger;

import android.content.Context;

import com.example.busroute.BusRouteApplication;
import com.example.busroute.data.NetworkRepository;
import com.example.busroute.domain.BusRepository;
import com.example.busroute.domain.interactor.usecase.BackgroundExecutionThread;
import com.example.busroute.domain.interactor.usecase.PostExecutionThread;
import com.example.busroute.executor.BackgroundThreadExecutor;
import com.example.busroute.executor.MainThreadExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final BusRouteApplication application;

    public ApplicationModule(BusRouteApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    BackgroundExecutionThread getBackgroundExecutionThread() {
        return new BackgroundThreadExecutor();
    }

    @Provides
    @Singleton
    PostExecutionThread getPostExecutionThread() {
        return new MainThreadExecutor();
    }

    @Provides
    @Singleton
    BusRepository provideBusRepository(NetworkRepository networkRepository) {
        return networkRepository;
    }
}
