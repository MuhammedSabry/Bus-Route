package com.example.busroute.executor;

import com.example.busroute.domain.interactor.usecase.PostExecutionThread;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author Muhammed Sabry
 */
public class MainThreadExecutor implements PostExecutionThread {
    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
