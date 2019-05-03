package com.example.busroute.domain.interactor.usecase;

import io.reactivex.Single;
import io.reactivex.SingleTransformer;

public abstract class SingleUseCase<R, Q> extends UseCase<Single, Q> {

    private final SingleTransformer<? super R, ? extends R> schedulersTransformer;

    protected SingleUseCase(final BackgroundExecutionThread backgroundExecutionThread, final PostExecutionThread postExecutionThread) {
        super(backgroundExecutionThread, postExecutionThread);
        schedulersTransformer = rObservable -> rObservable
                .subscribeOn(getBackgroundExecutionThread())
                .observeOn(getPostExecutionThread());
    }

    public Single<R> execute() {
        return execute(null);
    }

    @Override
    public Single<R> execute(Q param) {
        return interact(param).compose(applySchedulers());
    }

    @Override
    protected abstract Single<R> interact(Q param);

    private SingleTransformer<? super R, ? extends R> applySchedulers() {
        return schedulersTransformer;
    }
}