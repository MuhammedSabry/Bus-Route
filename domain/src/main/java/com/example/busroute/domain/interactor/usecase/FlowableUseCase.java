package com.example.busroute.domain.interactor.usecase;


import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;

/**
 * This class is supposed to make all your rx java operations and requests
 * be made in a background thread and then post the data to the ui thread
 *
 * @author Muhammed Sabry
 */
public abstract class FlowableUseCase<R, Q> extends UseCase<Flowable, Q> {

    private final FlowableTransformer<? super R, ? extends R> schedulersTransformer;

    protected FlowableUseCase(final BackgroundExecutionThread backgroundExecutionThread, final PostExecutionThread postExecutionThread) {
        super(backgroundExecutionThread, postExecutionThread);
        schedulersTransformer = rObservable -> rObservable
                .subscribeOn(getBackgroundExecutionThread())
                .observeOn(getPostExecutionThread());
    }

    public Flowable<R> execute() {
        return execute(null);
    }

    @Override
    public Flowable<R> execute(Q param) {
        return interact(param).compose(applySchedulers());
    }

    @Override
    protected abstract Flowable<R> interact(Q param);

    private FlowableTransformer<? super R, ? extends R> applySchedulers() {
        return schedulersTransformer;
    }
}