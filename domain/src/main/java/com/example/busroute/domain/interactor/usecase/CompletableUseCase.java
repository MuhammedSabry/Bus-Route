package com.example.busroute.domain.interactor.usecase;

import io.reactivex.Completable;
import io.reactivex.CompletableTransformer;

public abstract class CompletableUseCase<Q> extends UseCase<Completable, Q> {

    private final CompletableTransformer schedulersTransformer;

    protected CompletableUseCase(final BackgroundExecutionThread backgroundExecutionThread, final PostExecutionThread postExecutionThread) {
        super(backgroundExecutionThread, postExecutionThread);
        schedulersTransformer = rObservable -> rObservable
                .subscribeOn(getBackgroundExecutionThread())
                .observeOn(getPostExecutionThread());
    }

    protected Completable execute() {
        return execute(null);
    }

    @Override
    public Completable execute(Q param) {
        return interact(param).compose(applySchedulers());
    }

    @Override
    protected abstract Completable interact(Q param);

    private CompletableTransformer applySchedulers() {
        return schedulersTransformer;
    }
}