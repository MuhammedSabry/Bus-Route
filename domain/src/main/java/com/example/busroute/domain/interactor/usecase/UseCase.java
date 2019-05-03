package com.example.busroute.domain.interactor.usecase;

import io.reactivex.Scheduler;
import io.reactivex.annotations.Nullable;

/**
 * This class is supposed to hold a reference to background and foreground execution threads
 * to use them later for operations to be executed on a background thread then published to a post execution thread
 *
 * @author Muhammed Sabry
 */
public abstract class UseCase<P, Q> {

    private final BackgroundExecutionThread backgroundExecutionThread;
    private final PostExecutionThread postExecutionThread;

    UseCase(BackgroundExecutionThread backgroundExecutionThread, PostExecutionThread postExecutionThread) {
        this.backgroundExecutionThread = backgroundExecutionThread;
        this.postExecutionThread = postExecutionThread;
    }

    public abstract P execute(@Nullable Q param);

    protected abstract P interact(@Nullable Q param);

    Scheduler getBackgroundExecutionThread() {
        return backgroundExecutionThread.getScheduler();
    }

    Scheduler getPostExecutionThread() {
        return postExecutionThread.getScheduler();
    }
}